package pt.ornrocha.rtools.installutils.components;

import java.util.ArrayList;

public class RPackageInfo {
	
	public enum DevtoolsRepoType {

		github{
			
			@Override
			public String getDevToolsCMD(String repourl) {
				return "devtools::install_github("+repourl+")";
			}
			
			@Override
			public String getDevToolsCMD(String repourl, String usrlib) {
				return "devtools::install_github('"+repourl+"', args = c('--library="+usrlib+"'))";
				//return "devtools::install_github(\""+repourl+"\", args = c(\"--library="+usrlib+"\"))";
			}
		},
		bitbucket{
			@Override
			public String getDevToolsCMD(String repourl) {
				return "devtools::install_bitbucket("+repourl+")";
			}
			
			@Override
			public String getDevToolsCMD(String repourl, String usrlib) {
				return "devtools::install_bitbucket('"+repourl+"', args = c('--library="+usrlib+"'))";
			}
		},
		gitorious{
			@Override
			public String getDevToolsCMD(String repourl) {
				return "devtools::install_gitorious("+repourl+")";
			}
			
			@Override
			public String getDevToolsCMD(String repourl, String usrlib) {
				return "devtools::install_gitorious('"+repourl+"', args = c('--library="+usrlib+"'))";
			}
		},
		git{
			@Override
			public String getDevToolsCMD(String repourl) {
				return "devtools::install_git("+repourl+")";
			}
			
			@Override
			public String getDevToolsCMD(String repourl, String usrlib) {
				return "devtools::install_git('"+repourl+"', args = c('--library="+usrlib+"'))";
			}
		};
		
		
		public String getDevToolsCMD(String repourl) {
			return getDevToolsCMD(repourl);
		}
		
		public String getDevToolsCMD(String repourl, String usrlib) {
			return getDevToolsCMD(repourl, usrlib);
		}
		
	}
	
	public final static String RMAINREPO="http://cran.us.r-project.org";
	private String packagename=null;
	private boolean needsDevtools=false;
	private boolean bioconductorpackage=false;
	private String devtoolsrepositoryurl=null;
	private DevtoolsRepoType typerepo=null;
	private ArrayList<RPackageInfo> dependencies=new ArrayList<>();
	private String packagecranrepository=RMAINREPO;
    
	
	public RPackageInfo(String packagename) {
		this.packagename=packagename;
	};
	
	public RPackageInfo(String packagename,RPackageInfo...dependencies) {
		this.packagename=packagename;
		for (int i = 0; i < dependencies.length; i++) {
			this.dependencies.add(dependencies[i]);
		}
	};
	
	public RPackageInfo(String packagename,boolean isbioconductorpackage) {
		this.packagename=packagename;
		this.bioconductorpackage=isbioconductorpackage;
	}
	
	public RPackageInfo(String packagename,boolean isbioconductorpackage, RPackageInfo...dependencies) {
		this(packagename,dependencies);
		this.bioconductorpackage=isbioconductorpackage;
		
	}

	public RPackageInfo(String packagename,DevtoolsRepoType installfromdevtoolsrepo, String devtoolsrepourl) {
		this(packagename);
		this.needsDevtools=true;
		this.devtoolsrepositoryurl=devtoolsrepourl;
		this.typerepo=installfromdevtoolsrepo;
	}
	
	
	
	public RPackageInfo(String packagename,DevtoolsRepoType installfromdevtoolsrepo, String devtoolsrepourl,RPackageInfo...dependencies) {
		this(packagename,dependencies);
		this.needsDevtools=true;
		this.devtoolsrepositoryurl=devtoolsrepourl;
		this.typerepo=installfromdevtoolsrepo;
	}
	
	public RPackageInfo(String packagename,String devtoolsrepourl) {
		this(packagename);
		this.needsDevtools=true;
		this.devtoolsrepositoryurl=devtoolsrepourl;
		this.typerepo=DevtoolsRepoType.github;
	}
	
	public RPackageInfo(String packagename,String devtoolsrepourl,RPackageInfo...dependencies) {
		this(packagename,dependencies);
		this.needsDevtools=true;
		this.devtoolsrepositoryurl=devtoolsrepourl;
		this.typerepo=DevtoolsRepoType.github;
	}
	

	public boolean needsDevtools() {
		return needsDevtools;
	}

	public boolean isBioconductorPackage() {
		return bioconductorpackage;
	}

	public String getRepositoryUrl() {
		return devtoolsrepositoryurl;
	}

	public DevtoolsRepoType getTypeRepository() {
		return typerepo;
	}
	
	public String getDevtoolsInstallCommand() {
		if(devtoolsrepositoryurl!=null)
			return typerepo.getDevToolsCMD(devtoolsrepositoryurl);
		return null;
	}
	
	public String getDevtoolsInstallCommand(String usrlib) {
		if(devtoolsrepositoryurl!=null)
			return typerepo.getDevToolsCMD(devtoolsrepositoryurl,usrlib);
		return null;
	}

	public String getPackageName() {
		return packagename;
	}
	
	public String getPackageCranRepositoryURL() {
		return packagecranrepository;
	}

	public void setPackageCranRepositoryURL(String packagecranrepository) {
		this.packagecranrepository = packagecranrepository;
	}

	public ArrayList<String> getDependenciesPackageNames(){
		ArrayList<String> res=new ArrayList<>();
		for (int i = 0; i < dependencies.size(); i++) {
			res.add(dependencies.get(i).getPackageName());
		}
		return res;
	}
	
	public boolean needsDependencies() {
		return dependencies.size()>0?true:false;
	}
	
	public ArrayList<RPackageInfo> getPackageDependencies(){
		return dependencies;
	}

	public static RPackageInfo define(String packagename) {
		return new RPackageInfo(packagename);
	}
	
	public static RPackageInfo define(String packagename, RPackageInfo...dependencies) {
		return new RPackageInfo(packagename,dependencies);
	}
	
	public static RPackageInfo define(String packagename, boolean isbioconductorpackage){
		return new RPackageInfo(packagename,isbioconductorpackage);
	}
	
	public static RPackageInfo define(String packagename, boolean isbioconductorpackage,RPackageInfo...dependencies){
		return new RPackageInfo(packagename,isbioconductorpackage,dependencies);
	}
	
	public static RPackageInfo define(String packagename,String devtoolsrepourl) {
		return new RPackageInfo(packagename,devtoolsrepourl);
	}
	
	public static RPackageInfo define(String packagename,String devtoolsrepourl,RPackageInfo...dependencies) {
		return new RPackageInfo(packagename,devtoolsrepourl,dependencies);
	}
	
	public static RPackageInfo define(String packagename,DevtoolsRepoType installfromdevtoolsrepo, String devtoolsrepourl) {
		return new RPackageInfo(packagename,installfromdevtoolsrepo, devtoolsrepourl);
	}
	
	public static RPackageInfo define(String packagename,DevtoolsRepoType installfromdevtoolsrepo, String devtoolsrepourl,RPackageInfo...dependencies) {
		return new RPackageInfo(packagename,installfromdevtoolsrepo, devtoolsrepourl,dependencies);
	}
}
