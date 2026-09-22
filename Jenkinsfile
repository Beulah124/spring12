pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
        timestamps()
        disableConcurrentBuilds()
    }

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        DEPLOY_DIR = 'C:\\jenkins-deploy\\demo666'
        APP_PORT = '8081'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Versions') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        stage('Deploy JAR') {
            steps {
                bat '''
                    if not exist "%DEPLOY_DIR%" (
                        mkdir "%DEPLOY_DIR%"
                    )

                    copy /Y target\\demo666-0.0.1-SNAPSHOT.jar ^
                    "%DEPLOY_DIR%\\demo666.jar"
                '''
            }
        }

        stage('Start Application') {
            steps {
                withEnv(['JENKINS_NODE_COOKIE=dontKillMe']) {
                    bat '''
                        powershell -NoProfile -Command ^
                        "Start-Process -FilePath 'javaw.exe' ^
                        -ArgumentList '-jar','C:\\jenkins-deploy\\demo666\\demo666.jar','--server.port=8081'"
                    '''
                }
            }
        }

        stage('Wait for Application') {
            steps {
                sleep time: 10, unit: 'SECONDS'
            }
        }

        stage('Health Check') {
            steps {
                bat '''
                    powershell -NoProfile -Command ^
                    "$response = Invoke-WebRequest -UseBasicParsing 'http://localhost:8081/'; Write-Host $response.Content; if ($response.StatusCode -ne 200) { exit 1 }"
                '''
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true

            archiveArtifacts artifacts: 'target/*.jar',
                             fingerprint: true
        }

        success {
            echo 'Application built and started on port 8081.'
        }

        failure {
            echo 'Pipeline failed. Check Console Output.'
        }
    }
}
