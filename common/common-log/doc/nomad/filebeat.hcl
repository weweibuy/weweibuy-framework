
job "filebeat" {
  datacenters = ["*"]

  type = "system"
  update {
    min_healthy_time  = "10s"
    healthy_deadline  = "5m"
    progress_deadline = "10m"
    auto_revert       = true
  }

  group "filebeat" {

    # 这个Host类型挂载必须现在 client的配置文件中指定, 指定后才能使用!  
    # https://developer.hashicorp.com/nomad/tutorials/stateful-workloads/stateful-workloads-host-volumes
    volume "filebeat-data" {
      type = "host"
      # source 填写引用的client配置中的挂载名称
      source = "filebeat-data"
    }

    volume "nomad-alloc" {
      type = "host"
      source = "nomad-alloc"
      read_only = true
    }


    task "filebeat" {
      driver = "docker"

      volume_mount {
        volume      = "filebeat-data"
        destination = "/usr/share/filebeat/data"
      }

      volume_mount {
        volume      = "nomad-alloc"
        destination = "/nomad/alloc"
      }

      config {
        image = "elastic/filebeat:8.9.0"
        mount {
          type   = "bind"
          source = "local/filebeat.yml"
          target = "/usr/share/filebeat/filebeat.yml"
        }
      }
      resources {
        cpu    = 100
        memory = 100
      }
      template {
        data        = <<EOF

filebeat.autodiscover:
  providers:
    - type: nomad
      address: http://192.168.1.137:4646
      hints.enabled: true
      hints.default_config:
        enabled: false 
        type: log
        paths:
          - /nomad/alloc/${data.nomad.allocation.id}/${data.nomad.task.name}/local/*/application.log
        processors:
          - add_fields: 
              target: nomad
              fields:
                allocation.id: ${data.nomad.allocation.id}
    - type: nomad
      address: http://192.168.1.137:4646
      hints.enabled: true
      hints.default_config:
        enabled: false 
        type: log
        paths:
          - /nomad/alloc/${data.nomad.allocation.id}/${data.nomad.task.name}/local/*/application.json.log
        processors:
          - add_fields: 
              target: nomad
              fields:
                allocation.id: ${data.nomad.allocation.id}
          - decode_json_fields:
              fields: ['message']
              target: ""
              process_array: false
              max_depth: 1
              overwrite_keys: true

processors:
  - add_nomad_metadata: 
      when.has_fields.fields: [nomad.allocation.id]
      address: http://192.168.1.137:4646
      default_indexers.enabled: false
      default_matchers.enabled: false
      indexers:
        - allocation_uuid:
      matchers:
        - fields:
            lookup_fields:
              - 'nomad.allocation.id'
  - move_fields:
      from: "nomad"
      fields: ["namespace", "datacenter", "region" ]
      to: ""
  - rename:
      ignore_missing: true
      fail_on_error: false
      fields:
        - from: "nomad.task.name"
          to: "task_name"
        - from: "nomad.allocation.id"
          to: "instance"
        - from: "@timestamp"
          to: "collection_time"

  - drop_fields:
      fields: ["ecs", "agent", "input", "log", "host", "@metadata", "nomad"]

output.console:
  pretty: true	
EOF
        destination = "local/filebeat.yml"

      }
    }
  }
}
