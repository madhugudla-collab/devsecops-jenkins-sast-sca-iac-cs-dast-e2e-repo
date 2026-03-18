pipeline {
  agent any
  tools {
    maven 'Maven_3_8_7'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }
    stage('CompileandRunSonarAnalysis') {
      steps {
        withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
          bat("mvn -e -Dmaven.test.failure.ignore verify sonar:sonar -Dsonar.login=%SONAR_TOKEN%  -Dsonar.projectKey=easybuggy -Dsonar.host.url=http://localhost:9000/")
        }
      }
    }
    stage('Build') {
      steps {
        withDockerRegistry([credentialsId: "dockerlogin", url: ""]) {
          script {
            app = docker.build("asecurityguru/testeb")
          }
        }
      }
    }
    stage('RunContainerScan') {
      steps {
        withCredentials([string(credentialsId: 'SNYK_TOKEN', variable: 'SNYK_TOKEN')]) {
          script {
            try {
              bat("C:\\Users\\madhu\\DevSecOps\\snyk-windows\\snyk-win.exe  container test asecurityguru/testeb")
            } catch (err) {
              echo err.getMessage()
            }
          }
        }
      }
    }
    stage('RunSnykSCA') {
      steps {
        withCredentials([string(credentialsId: 'SNYK_TOKEN', variable: 'SNYK_TOKEN')]) {
          bat("mvn snyk:test -fn")
        }
      }
    }
    stage('RunDASTUsingZAP') {
      steps {
        dir("C:\\Users\\madhu\\DevSecOps\\ZAPCrossplatform\\ZAP_2.16.0") {
          bat "zap.bat -port 9393 -cmd -quickurl https://www.example.com -quickprogress -quickout %WORKSPACE%\\Output.html"
        }
      }
    }

    stage('checkov') {
      steps {
        bat("checkov -s -f main.tf")
      }
    }

  }

  post {
    always {
      script {
        // Call Security Orchestrator Bot using Groovy httpRequest
        // Uses JsonOutput.toJson() to safely handle Windows paths (backslashes etc.)
        // Bot reads Jenkins console log and creates GitHub Issues/PRs per scan type:
        //   SAST (SonarQube)  -> GitHub PR with AI code fix
        //   SCA (Snyk)        -> GitHub Issue alert
        //   Container (Trivy) -> GitHub Issue alert
        //   DAST (ZAP)        -> Saves JSON report
        //   IaC (Checkov)     -> GitHub Issue with remediation steps
        try {
          def payload = groovy.json.JsonOutput.toJson([
            build_url             : env.BUILD_URL,
            job_name              : env.JOB_NAME,
            repo_owner            : "madhugudla-collab",
            repo_name             : "devsecops-jenkins-sast-sca-iac-cs-dast-e2e-repo",
            branch                : "main",
            jenkins_user          : "admin",
            jenkins_workspace     : env.WORKSPACE,
            sonarqube_url         : "http://localhost:9000",
            sonarqube_project_key : "easybuggy"
          ])

          def response = httpRequest(
            url              : 'http://127.0.0.1:8000/webhook/jenkins/pipeline',
            httpMode         : 'POST',
            contentType      : 'APPLICATION_JSON',
            requestBody      : payload,
            validResponseCodes: '200:299',
            timeout          : 30
          )
          echo "Security Bot Response: ${response.status} - ${response.content}"
        } catch (err) {
          echo "Security bot notification failed: ${err.getMessage()} — build result unchanged"
        }
      }
    }
  }
}
