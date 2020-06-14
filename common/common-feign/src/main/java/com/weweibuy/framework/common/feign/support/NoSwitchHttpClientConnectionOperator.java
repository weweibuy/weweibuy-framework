package com.weweibuy.framework.common.feign.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

import java.io.IOException;
import java.net.*;

/**
 * 不切换DNS 节点
 * HttpClientConnectionOperator 在连接超时时会, 自动切换节点, 直到有一个连接成功
 * 该类不切换节点,当发生连接超时时直接抛出异常
 *
 * @author durenhao
 * @date 2020/6/14 10:59
 **/
@Slf4j
public class NoSwitchHttpClientConnectionOperator implements HttpClientConnectionOperator {

    static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";

    private final Lookup<ConnectionSocketFactory> socketFactoryRegistry;
    private final SchemePortResolver schemePortResolver;
    private final DnsResolver dnsResolver;

    public NoSwitchHttpClientConnectionOperator(
            final Lookup<ConnectionSocketFactory> socketFactoryRegistry,
            final SchemePortResolver schemePortResolver,
            final DnsResolver dnsResolver) {
        Args.notNull(socketFactoryRegistry, "Socket factory registry");
        this.socketFactoryRegistry = socketFactoryRegistry;
        this.schemePortResolver = schemePortResolver != null ? schemePortResolver :
                DefaultSchemePortResolver.INSTANCE;
        this.dnsResolver = dnsResolver != null ? dnsResolver :
                SystemDefaultDnsResolver.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    private Lookup<ConnectionSocketFactory> getSocketFactoryRegistry(final HttpContext context) {
        Lookup<ConnectionSocketFactory> reg = (Lookup<ConnectionSocketFactory>) context.getAttribute(
                SOCKET_FACTORY_REGISTRY);
        if (reg == null) {
            reg = this.socketFactoryRegistry;
        }
        return reg;
    }

    @Override
    public void connect(
            final ManagedHttpClientConnection conn,
            final HttpHost host,
            final InetSocketAddress localAddress,
            final int connectTimeout,
            final SocketConfig socketConfig,
            final HttpContext context) throws IOException {
        final Lookup<ConnectionSocketFactory> registry = getSocketFactoryRegistry(context);
        final ConnectionSocketFactory sf = registry.lookup(host.getSchemeName());
        if (sf == null) {
            throw new UnsupportedSchemeException(host.getSchemeName() +
                    " protocol is not supported");
        }

        final InetAddress[] addresses = host.getAddress() != null ?
                new InetAddress[]{host.getAddress()} : this.dnsResolver.resolve(host.getHostName());
        final int port = this.schemePortResolver.resolve(host);

        final InetAddress address = addresses[0];
        Socket sock = sf.createSocket(context);
        sock.setSoTimeout(socketConfig.getSoTimeout());
        sock.setReuseAddress(socketConfig.isSoReuseAddress());
        sock.setTcpNoDelay(socketConfig.isTcpNoDelay());
        sock.setKeepAlive(socketConfig.isSoKeepAlive());
        if (socketConfig.getRcvBufSize() > 0) {
            sock.setReceiveBufferSize(socketConfig.getRcvBufSize());
        }
        if (socketConfig.getSndBufSize() > 0) {
            sock.setSendBufferSize(socketConfig.getSndBufSize());
        }

        final int linger = socketConfig.getSoLinger();
        if (linger >= 0) {
            sock.setSoLinger(true, linger);
        }
        conn.bind(sock);

        final InetSocketAddress remoteAddress = new InetSocketAddress(address, port);
        if (this.log.isDebugEnabled()) {
            this.log.debug("Connecting to " + remoteAddress);
        }
        try {
            sock = sf.connectSocket(
                    connectTimeout, sock, host, remoteAddress, localAddress, context);
            conn.bind(sock);
            if (this.log.isDebugEnabled()) {
                this.log.debug("Connection established " + conn);
            }
            return;
        } catch (final SocketTimeoutException ex) {
            throw new ConnectTimeoutException(ex, host, addresses);
        } catch (final ConnectException ex) {
            final String msg = ex.getMessage();
            throw "Connection timed out".equals(msg)
                    ? new ConnectTimeoutException(ex, host, addresses)
                    : new HttpHostConnectException(ex, host, addresses);
        } catch (final NoRouteToHostException ex) {
            throw ex;
        }
    }

    @Override
    public void upgrade(
            final ManagedHttpClientConnection conn,
            final HttpHost host,
            final HttpContext context) throws IOException {
        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        final Lookup<ConnectionSocketFactory> registry = getSocketFactoryRegistry(clientContext);
        final ConnectionSocketFactory sf = registry.lookup(host.getSchemeName());
        if (sf == null) {
            throw new UnsupportedSchemeException(host.getSchemeName() +
                    " protocol is not supported");
        }
        if (!(sf instanceof LayeredConnectionSocketFactory)) {
            throw new UnsupportedSchemeException(host.getSchemeName() +
                    " protocol does not support connection upgrade");
        }
        final LayeredConnectionSocketFactory lsf = (LayeredConnectionSocketFactory) sf;
        Socket sock = conn.getSocket();
        final int port = this.schemePortResolver.resolve(host);
        sock = lsf.createLayeredSocket(sock, host.getHostName(), port, context);
        conn.bind(sock);
    }

}
