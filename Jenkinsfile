pipeline {
    agent any

    tools {
        maven 'Maven3' // Ensure this matches the name in Global Tool Configuration
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/azrardih76/learningJenkins'
            }
        }

        stage('Build') {
            steps {
                sh "mvn -Dmaven.test.failure.ignore=true clean package"
            }
            post {
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage("Deploy to QA") {
            steps {
                echo "Deploy to QA"
            }
        }

        stage('Regression Automation Test') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh "mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunners/testng_regressions.xml"
                }
            }
        }

        stage('Publish Extent Report') {
            steps {
                // Copy the report to the HTTP server directory
                sh "cp build/TestExecutionReport.html /path/to/http/server/directory"

                // Publish the HTML report with a link to the HTTP server
                publishHTML([allowMissing: false,
                             alwaysLinkToLastBuild: true,
                             keepAll: true,
                             reportDir: '/path/to/http/server/directory',
                             reportFiles: 'TestExecutionReport.html',
                             reportName: 'HTML Extent Report',
                             reportTitles: ''])
            }
        }

        stage('Jira Integration') {
            steps {
                jiraSendBuildInfo site: 'https://manulife-asia.atlassian.net/',
                                  projectKey: 'IAM-5140',
                                  buildNumber: currentBuild.number,
                                  buildResult: currentBuild.result,
                                  credentialsId: 'jira-jenkins'
            }
        }

        stage('Update Jira Issue with Report Link') {
            steps {
                script {
                    def reportUrl = "${env.JENKINS_URL}job/${env.JOB_NAME}/${env.BUILD_NUMBER}/HTML_20Report/TestExecutionReport.html"
                    jiraIssueUpdater site: 'https://manulife-asia.atlassian.net/',
                                     issueKey: 'IAM-5140',
                                     comment: "The HTML Extent Report for this build can be accessed here.",
                                     credentialsId: 'jira-jenkins'
                }
            }
        }
    }
}