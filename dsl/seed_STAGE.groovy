pipelineJob(JobName) {
	description()
	keepDependencies(false)
    parameters
    {
        stringParam('tag_number', '', 'Please give the tag number for the build artifacts for versioning in artifactory. For e.g. release-1.0')
        booleanParam('Functional_Testing', false, 'Check this if you want to perform functional testing as part of CI/CD')
    }
    environmentVariables {
		env("Build_Type", BuildType)
        env("Application_Repo_Url", AppRepoUrl)
        env("Application_Repo_Branch", AppRepoBranch)
        env("environment_type", 'STAGE')
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
          scriptPath('StageJenkinsfile/Jenkinsfile')
	}
	disabled(false)
}
}
