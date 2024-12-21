resource "random_uuid" "name" {
  
}

locals {
  name=random_uuid.name.id
}

resource "tls_private_key" "key" {
  algorithm = "RSA"
  rsa_bits = 4096
}

resource "google_compute_instance" "vm" {
  name = "vm-${local.name}"

  machine_type = "e2-small"

  boot_disk {
    auto_delete = true
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2404-noble-amd64-v20241115"
      size = 20
      type = "pd-standard"
    }
    mode = "READ_WRITE"
  }

  network_interface {
    access_config {
      network_tier = "STANDARD"
    }
    network = "default"
  }

  tags = ["https-server", "http-server"]

  zone = "us-central1-c"

  metadata = {
    ssh-keys = "user:${tls_private_key.key.public_key_openssh}"
  }

  provisioner "remote-exec" {
    connection {
      type = "ssh"
      user = "user"
      host = google_compute_instance.vm.network_interface[0].access_config[0].nat_ip
      private_key = tls_private_key.key.private_key_openssh
    }
    inline = [
      "curl -fsSL https://get.docker.com -o get-docker.sh",
      "sudo sh get-docker.sh"
    ]
  }
}

resource "terraform_data" "runContainer" {
    provisioner "remote-exec" {
      connection {
        type = "ssh"
        user = "user"
        host = google_compute_instance.vm.network_interface[0].access_config[0].nat_ip
        private_key = tls_private_key.key.private_key_openssh
      }
      inline = [
        "sudo docker pull registry.gitlab.com/lsit-ken3239/live-coding-spaces/lsitG-beer:latest",
        "sudo docker stop app",
        "sudo docker rm app",
        "sudo docker run -p80:8080 --name app -d registry.gitlab.com/lsit-ken3239/live-coding-spaces/lsitG-beer:latest"
      ]
  }
}

output "service_url" {
  value = "http://${google_compute_instance.vm.network_interface[0].access_config[0].nat_ip}"
}
