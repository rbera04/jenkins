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
                    sh 'cd myfirstrepo/my-app && mvn clean && mvn package'
		        }
		    }
            stage ('Test'){
                steps {
                    sh 'cd myfirstrepo/my-app && java -cp target/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App'
                }  
		    }
		}
	}
