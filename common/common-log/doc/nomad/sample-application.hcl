job "sample-application" {
    datacenters = ["dc1"]

    type = "service"



    group "sample-application" {
        
        count = 1

        network {
            port "http" {
                to = "8080"
            }
        }

        service {
            name = "sample-application"   
            port     = "http"
            provider = "nomad" 
        }


        task "sample-application" {

            driver = "docker"

            config {
                image = "registry.cn-qingdao.aliyuncs.com/weweibuy/sample-application:1.0"
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
                "co.elastic.logs/multiline.pattern" = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}"
                "co.elastic.logs/multiline.negate"  = "true"
                "co.elastic.logs/multiline.match"   = "after"
            }


        }

    }

}