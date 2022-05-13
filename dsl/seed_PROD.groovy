pipelineJob(JobName) {
	description()
	keepDependencies(false)
    parameters
    {
        stringParam('Artifact_Version_Number', '', '<b> <p style="color:blue;">Please give the exact Artifact_Version_Number from Artifactory which you need to deploy. Eg- You can check your artifacts in the respective repository in Release folder. It should be like PackageName-GitTagNumber, so you just to need to pass the GitTagNumber in Build with Parameter value.</b>')
        booleanParam('Rollback_option', false, '<b> <p style="color:red;">Rollback option is checked. If the deployment fails it will redeploy the previous version. </b>')
        stringParam('Rollback_Artifact_Version_Number', '', '<b><p style="color:red;">Please give the previous Artifact_Version_Number from Artifactory incase of Rollback to previous artifact version number if the current deployment fails</b>')
        booleanParam('Change_Management', false, '<b> <p style="color:green;">Please Check this option if you want to implement JIRA Change Management so that the deployment will be done based on the status of the ticket</b>')
        stringParam('JIRA_ID', '', '<b><p style="color:green;">Please give the JIRA ticket number. For e.g. OITEA-293</b>')


    }
    environmentVariables {
        env("Application_Repo_Url", AppRepoUrl)
        env("Application_Repo_Branch", AppRepoBranch)
        env("environment_type", 'PROD')
		groovy()
		loadFilesFromMaster(false)
		keepSystemVariables(true)
		keepBuildVariables(true)
		overrideBuildParameters(false)
	}
	definition {
		cpsScm {
                          scm {
                    git {
                        remote {
                            url('https://orahub.oci.oraclecorp.com/oit-entapps-devops/jenkins-pipeline.git')
                            credentials('12b63efe-bcf7-44b3-a11f-a6c3c4faa23e')
                        }
                        branch('master')
                        extensions{
                            cleanAfterCheckout()
                            cloneOptions {
                                noTags(true)
                                honorRefspec(true)
                                shallow(true)
                                depth(1)
                            }
                        }
                    }
		}
          scriptPath('ProdJenkinsfile/Jenkinsfile')
	}
	disabled(false)
}
}
