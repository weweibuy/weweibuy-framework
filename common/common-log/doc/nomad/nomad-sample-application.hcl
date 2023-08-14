job "nomad-sample-application" {
    datacenters = ["dc1"]

    type = "service"

    group "nomad-sample-application" {
        
        count = 1

        network {
            port "http" {
                to = "8080"
            }
        }

        service {
            name = "nomad-sample-application"   
            port     = "http"
        #    provider 服务注册的提供者 默认是 consul, 由consul提供注册发现
        #    provider = "nomad" 
        }


        task "nomad-sample-application" {

            driver = "docker"

            config {
                image = "registry.cn-qingdao.aliyuncs.com/weweibuy/nomad-sample-application:1.0"
                ports = ["http"]

                mount {
                    type = "bind"
                    source = "local"
                    target = "/logs"
                }
            }


            resources {
                cpu = 100
                memory = 500
            }
            
            meta = {
                "co.elastic.logs/enabled"           = "true"
            }


        }

    }

}