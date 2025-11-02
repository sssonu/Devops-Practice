pipeline {
    agent any

    tools {
        // Specify any tools needed, e.g., for Maven, Gradle, etc.
        // For this example, we'll rely on the Docker client in the Jenkins container
    }

    stages {
        stage('Checkout Source Code') {
            steps {
                script {
                    // Replace with your actual Git repository URL
                    // Make sure this repo contains your my-webapp directory and its contents
                    // For a quick test, you can initially point it to a local directory,
                    // but for CI/CD, a remote Git repo is standard.
                    git branch: 'main', url: 'https://github.com/sssonu/Devops-Practice.git'
                    // Or for local testing (less common for CI/CD):
                    // dir('my-webapp') {
                    //    sh 'cp -r /var/jenkins_home/workspace/my-pipeline/my-webapp .'
                    // }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dir('my-webapp') { // Change to your application's directory
                        echo "Building Docker image for my-webapp..."
                        sh 'docker build -t my-webapp-image:latest .'
                        echo "Docker image built: my-webapp-image:latest"
                    }
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    echo "Stopping any existing my-webapp-container..."
                    sh 'docker stop my-webapp-container || true' // || true to prevent script from failing if container doesn't exist
                    sh 'docker rm my-webapp-container || true'

                    echo "Running new Docker container for my-webapp..."
                    sh 'docker run -d -p 8081:5000 --name my-webapp-container my-webapp-image:latest'
                    echo "my-webapp-container is running on port 8081 (host) -> 5000 (container)"
                    sh 'docker ps -a'
                }
            }
        }

        stage('Test Application (Optional)') {
            steps {
                script {
                    echo "Waiting a few seconds for the app to start..."
                    sleep 10
                    echo "Testing if the application is reachable..."
                    sh 'curl http://localhost:8081' // This will test the application from inside the Jenkins container
                                                  // Make sure the Jenkins container can reach the host network or the specific container.
                                                  // For production, you'd use a more robust testing framework.
                }
            }
        }
    }
    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
