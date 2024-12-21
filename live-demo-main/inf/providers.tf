terraform {
  required_version = ">0.13"

  backend "http"{
    
  }

  required_providers {
    google = {
        source   = "hashicorp/google"
        version  = "6.12.0"
    }

    random = {
        source  = "hashicorp/random"
        version = "~>3.0"
    }
  }
}

provider "google" {
  project = "lsit-beer"
  region = "us-central1"
}