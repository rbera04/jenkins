pipeline {
    agent { label 'agent' }
    stages {
        stage('Repo clone') {
            steps {
                echo 'This is build stage'
                sh 'rm -rf *'
                sh 'git clone -b maven https://github.com/madandevops11/myfirstrepo.git'
				  }
			}
		    stage ('Build'){
                steps {
                    echo 'This is validate stage'
                    sh 'pwd'
                    sh 'cd myfirstrepo/my-app && mvn clean && mvn clean install'
		        }
		    }
            stage ('Test'){
                steps {
                    sh 'echo "skip"'
                    // sh 'cd myfirstrepo/my-app && java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App'
                }  
		    }
            stage('Docker build and publish') {
                steps {
                    sh 'git clone -b master https://github.com/rbera04/docker.git'
                    sh 'ls -lart myfirstrepo/my-app/target/'
                    sh 'cp docker/java.dockerfile myfirstrepo/my-app/'
                    sh 'ls -lart myfirstrepo/my-app/'
                    sh 'cat myfirstrepo/my-app/java.dockerfile'
                    sh 'cd myfirstrepo/my-app && docker build -t rbera08/myapp:v1 -f java.dockerfile .'
                    // sh 'docker login -u ${USERNAME} -p ${PASSWORD} ${DOCKER_REPOSITORY}'
                    withCredentials([string(credentialsId: 'docker', variable: 'DOCKER_REPOSITORY')]) {
                        sh 'docker login --username=rbera08 --password-stdin <<<${DOCKER_REPOSITORY}'
                        sh 'docker push rbera08/myapp:v1'
                    }
                }
            }
            stage('deploy'){
                steps {
                    // sh 'docker login -u="${DOCKER_USERNAME}" -p="${DOCKER_PASSWORD}"'
                    // sh 'docker pull rbera08/myapp:v1'
                    withCredentials([string(credentialsId: 'docker', variable: 'DOCKER_REPOSITORY')]) {
                        sh 'docker login --username=rbera08 --password-stdin <<<${DOCKER_REPOSITORY}'
                         sh 'docker run -dit -p 8085:80 rbera08/myapp:v1'
                    }
                   
                }
            }
		}
	}
