pipeline {
    agent any
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
                    sh 'cd myfirstrepo/my-app && mvn clean && mvn package'
		        }
		    }
            stage ('Test'){
                steps {
                    sh 'cd myfirstrepo/my-app && java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App'
                }  
		    }
            stage('Docker build and publish') {
                steps {
                    sh 'git clone -b master https://github.com/rbera04/docker.git'
                    sh 'cp java.dockerfile myfirstrepo/my-app/'
                    sh 'cd myfirstrepo/my-app && docker build -t rbera08/myapp:v1 .'
                    sh 'docker push rbera08/myapp:v1'
                }
            }
            stage('deploy'){
                steps {
                    sh 'docker pull rbera08/myapp:v1'
                    sh 'docker run -dit -p 8085:80 myapp:v1'
                }
            }
		}
	}
