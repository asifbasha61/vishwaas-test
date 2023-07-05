#!/bin/bash

# Check if node is already installed
echo "Validating Node installation..."
if [ -x "$(command -v node)" ]; then
    node_version=$(node --version)
    echo "Node is already installed with the version: $node_version"

else
    echo "Starting Node installation..."
    # Install Node
    curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash - &&\
    sudo apt-get install -y nodejs
fi

# Check if Docker is already installed
echo "Validating Docker installation..."
if [ -x "$(command -v docker)" ]; then
    docker_version=$(docker --version | awk '{print $3}')
    echo "Docker is already installed with the version: $docker_version"

else
    echo "Starting Docker installation..."
    # Install prerequisite packages
    sudo apt install -y apt-transport-https ca-certificates curl software-properties-common

    # Add the Docker repository to APT sources
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu `lsb_release -cs` test"

    # Update package database with Docker packages from the newly added repo
    sudo apt update > /dev/null 2>&1

    # Install Docker
    sudo apt install -y docker-ce

    # Add the current user to the docker group
    sudo usermod -aG docker ${USER}
    docker_installed_version=$(docker --version | awk '{print $3}')
    echo "Docker installed successfully with the version: $docker_installed_version"
fi

echo "Validating Docker Compose installation..."
# Check if Docker Compose is already installed
if [ -x "$(command -v docker-compose)" ]; then
    docker_compose_version=$(docker-compose version --short)
    echo "Docker Compose is already installed with the version: $docker_compose_version"
else
    echo "Starting Docker Compose installation..."
    # Install Docker Compose
    sudo curl -L https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose | tee /dev/tty

    # Set the permissions
    sudo chmod +x /usr/local/bin/docker-compose

    echo "Docker Compose version $(docker-compose version --short) installed successfully."
fi

# Check if registry is already installed
echo "Validating Registry installation..."
if [ -x "$(command -v registry)" ]; then
    registry_version=$(registry version)
    echo "Registry is already installed with the version: $registry_version"
    echo "Please continue using Registry"

else
    echo "Starting Registry installation..."
    # Install Registry
    npm install --global registry-cli
    registry help
    echo "Registry installed successfully"

fi

