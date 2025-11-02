pipeline {
    agent any

    stages {
        stage('Checkout Source Code') {
            steps {
                script {
                    git branch: 'main', url: 'https://github.com/sssonu/Devops-Practice.git'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dir('my-webapp') {
                        echo "Building Docker image for my-webapp..."
                        // CHANGE THIS LINE
                        sh 'bash -c "docker build -t my-webapp-image:latest ."'
                        echo "Docker image built: my-webapp-image:latest"
                    }
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    echo "Stopping any existing my-webapp-container..."
                    // CHANGE THESE LINES
                    sh 'bash -c "docker stop my-webapp-container || true"'
                    sh 'bash -c "docker rm my-webapp-container || true"'

                    echo "Running new Docker container for my-webapp..."
                    // CHANGE THIS LINE
                    sh 'bash -c "docker run -d -p 8081:5000 --name my-webapp-container my-webapp-image:latest"'
                    echo "my-webapp-container is running on port 8081 (host) -> 5000 (container)"
                    // CHANGE THIS LINE
                    sh 'bash -c "docker ps -a"'
                }
            }
        }

        stage('Test Application (Optional)') {
            steps {
                script {
                    echo "Waiting a few seconds for the app to start..."
                    sleep 10
                    echo "Testing if the application is reachable..."
                    // CHANGE THIS LINE
                    sh 'bash -c "curl http://localhost:8081"'
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
