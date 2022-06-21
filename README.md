



# CA-FACER

CA-FACER is a Context-aware API usage-based Code Recommender tool; an Intellij IDEA plugin that leverages a developer's development context to recommend related code snippets. We consider the methods having API usages in a developer's active project as part of the development context. Our approach uses contextual data from a developer's active project to find similar projects and recommends code from popular features of those projects. The popular features are identified as frequently occurring API usage based method clone classes.


Please follow the steps below for detailed setup guidelines.


## CA-FACER Plugin Demo
The demo for our tool can be viewed on YouTube [here](https://www.youtube.com/watch?v=UjuM8WRc318).



### Pre-requisites

Download and install the following dependencies: 

IntelliJ IDEA 2020.1.4 (Community Edition)
Build #IC-201.8743.12, built on July 21, 2020


### Resources

1. Plugin Installer Package (FACER-AS-1.05.zip)
2. Resources - Download from https://bit.ly/3zltzSE

Extract the contents of the FACER-AS Resources RAR file to a folder in local file system. The path to this folder will be configured in the plugin

### Installation

In order to install this plugin snapshot in Intellij, launch Intellij IDEA and go to **File > Settings > Plugins > Settings Icon > Install Plugin from Disk...**, and choose the Plugin Installer Package zip file downloaded earlier. Restart Intellij to show the FACER option in top Menu bar. 



## Plugin Configuration
On first launch of CA-FACER, the Configuration Setup dialog will appear (or use FACER on menu bar and select Configuration option). Add the path to the resources root folder that we extracted earlier. This root folder must contain the Dataset folder, LuceneIndex folder and the stopwords.txt file provided in the FACER-AS Resources. This folder will also be used as a destination for the User Interaction logs. After the setup is complete, click Ok option to update the plugin configuration settings. 

## Related Research

Details of the FACER system can be found in the following research papers:

1. Abid, S., Shamail, S., Basit, H. A., & Nadi, S. (2021). FACER: An API usage-based code-example recommender for opportunistic reuse. Empirical Software Engineering, 26(6), 1-58.

2. Abid, S. (2019, August). Recommending related functions from API usage-based function clone structures. In _Proceedings of the 2019 27th ACM Joint Meeting on European Software Engineering Conference and Symposium on the Foundations of Software Engineering_ (pp. 1193-1195).  

If you use FACER plugin for your research, please cite these articles.
    

