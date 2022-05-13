folder(folderName) {
  description('Folder Created for '+folderName)
}

def list = ['DEV','STAGE','QA','PROD','DEVDB','STAGEDB','QADB','PRODDB']
for (item in list)
{
folder(folderName+'/'+"${item}") {
  description('Folders created for '+"${item}"+' environment')
}
}


listView(folderName) {
    description('All DEV,STAGE,QA,PROD,DEVDB,STAGEDB,QADB,PRODDB seed and CI/CD pipeline jobs for '+folderName+' application')
    filterBuildQueue()
    filterExecutors()
 for (item in list)
  {
    jobs {
          recurse()
          name(folderName+'/'+"${item}")
    }
    
    job(folderName+'/'+"${item}"+'/'+"${item}"+'-Template') {
	description('This job will create a '+"${item}"+' Pipeline based on the parameters passed by the user')
	keepDependencies(false)
  if ("${item}" == 'DEV' || "${item}" == 'QA')
  {
	parameters {
		stringParam("JobName", "", "Please enter the JobName for the pipeline which you want to create")
		choiceParam("BuildType", ["Maven", "Npm", "Maven-Npm"], "Please Select the choice of your build type")
		stringParam("AppRepoUrl", "", "Enter the url for the repo where application related code is stored")
		stringParam("AppRepoBranch", "", 'Please enter the branch name of your application repo which you want to build')
		activeChoiceParam("PollingMechanism") {
			description('<b> <p style="color:blue;">Note : - It is Mandatory to implement Webhook or Pollscm. <br>If webhook is selected then it will trigger the pipeline job everytime on code commit. <br>If PollSCM is selected then it will check for new commit every 15 minutes and run the job</b>')
			groovyScript {
				script("return ['Webhook','PollSCM']")
				fallbackScript("return ['NotFound']")
				//sandbox(true)
			}
			choiceType("RADIO")
			filterable(false)
		}

		booleanParam('Sonarqube_Analysis', false, 'Check this if you want to implement sonarqube static code analysis')
		booleanParam('Functional_Testing', false, 'Check this if you want to perform functional testing as part of CI/CD')
		booleanParam('SAST_Implementation', false, 'Check this if you want to get your repos scanned for code vulnerabilities and misconfigurations')		
		activeChoiceParam("UnitTest_Coverage") {
			description("Select if your code has unit test coverage enabled")
			groovyScript {
				script("return [\"no\",\"yes\"]")
				fallbackScript("return [\"NotFound\"]")
				//sandbox(true)
			}
			choiceType("SINGLE_SELECT")
			filterable(false)
		}
		activeChoiceReactiveParam("Coverage_Value") {
			description("Select the minimum line coverage value to set")
			groovyScript {
				script("return [\"Select\",\"40\",\"60\",\"80\"]")
				fallbackScript("return [\"NotFound\"]")
				//sandbox(true)
			}
			referencedParameter("UnitTest_Coverage")
			choiceType("SINGLE_SELECT")
			filterable(false)
		}
	}
  }
  if ("${item}" == 'STAGE')
  {
	  parameters {
		stringParam("JobName", "", "Please enter the JobName for the pipeline which you want to create")
		choiceParam("BuildType", ["Maven", "Npm", "Maven-Npm"], "Please Select the choice of your build type")
		stringParam("AppRepoUrl", "", "Enter the url for the repo where application related code is stored")
		stringParam("AppRepoBranch", "", "Please enter the branch name of your application repo which you want to build")
	}

  }
  if ("${item}" == 'PROD')
  {
   parameters {
       stringParam("JobName", "", "Please enter the JobName for the pipeline which you want to create")
	   stringParam("AppRepoUrl", "", "Enter the url for the repo where application related code is stored")
	   stringParam("AppRepoBranch", "", "Please enter the branch name of your application repo which you want to build")

  }
  }
    if ("${item}" == 'DEVDB' || "${item}" == 'STAGEDB' || "${item}" == 'QADB' || "${item}" == 'PRODDB')
   {
   parameters {
       stringParam("JobName", "", "Please enter the JobName for the pipeline which you want to create")
	   stringParam("DBAppRepoUrl", "", "Enter the url for the repo where database application related code is stored")
	   stringParam("DBAppRepoBranch", "", "Please enter the branch name of your database application repo which you want to build")
	   stringParam("order_file_name", "", "Please enter the order file name as per your DB schema.PS-This step is optional when you have multiple order file in 1 repo")

  }
  }
	scm {
		git {
			remote {
				url('https://orahub.oci.oraclecorp.com/oit-entapps-devops/jenkins-pipeline.git')
				credentials('e0a06baa-a197-4f27-9210-c4a28d35511a')
			}
			branch("*/master")
		}
	}
	disabled(false)
	concurrentBuild(false)
	steps {
		dsl {
            external('dsl'+'/'+'seed_'+"${item}"+'.groovy')
			ignoreExisting(false)
			removeAction("IGNORE")
			removeViewAction("IGNORE")
			lookupStrategy("SEED_JOB")
		}
	}
	wrappers {
		preBuildCleanup {
			deleteDirectories(false)
			cleanupParameter()
		}
	}
}

  }
  columns {
		status()
		weather()
		name()
		lastSuccess()
		lastFailure()
		lastDuration()
		buildButton()
	}
	}
