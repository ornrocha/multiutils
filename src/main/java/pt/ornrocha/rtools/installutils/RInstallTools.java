/************************************************************************** 
 * Orlando Rocha (ornrocha@gmail.com)
 *
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 *  
 */
package pt.ornrocha.rtools.installutils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import com.github.sarxos.winreg.HKey;
import com.github.sarxos.winreg.RegistryException;
import com.github.sarxos.winreg.WindowsRegistry;
import com.vdurmont.semver4j.Semver;

import pt.ornrocha.fileutils.MTUDirUtils;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.rtools.installutils.components.InstallationProgressionChecker;
import pt.ornrocha.rtools.installutils.components.PackageInstalledChecker;
import pt.ornrocha.rtools.installutils.components.RLibsPathChecker;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.rtools.installutils.components.SimpleCMDOutputChecker;
import pt.ornrocha.swingutils.progress.AbstractProcessProgressionChecker;
import pt.ornrocha.systemutils.OSystemUtils;
import pt.ornrocha.systemutils.OSystemUtils.OS;

public class RInstallTools {
	
	
	public final static String R_HOME_KEY = "R_HOME";
	//public final static String RMAINREPO="http://cran.us.r-project.org";
	public final static String RBIOCONDUCTORSOURCE="source('https://bioconductor.org/biocLite.R')";
	//public final static String RBIOCONDUCTORSOURCEWIN="source('https://bioconductor.org/biocLite.R')";
	public final static String BIOCLITE="biocLite(suppressUpdates=TRUE,suppressAutoUpdate=TRUE)";
	public final static String NOUPDATESTAG="suppressUpdates=TRUE, suppressAutoUpdate=TRUE";
	public final static String WINSP="'";
	public final static String LINSP="\"";
	
	
	public static boolean installPackage(RPackageInfo packageinfo) throws Exception{
		return installPackage(packageinfo, null);
	}
	
	
	public static boolean installPackage(RPackageInfo packageinfo,String toLib) throws Exception{
		
		if(packageinfo.needsDependencies()) {
			ArrayList<RPackageInfo> depends=packageinfo.getPackageDependencies();
			for (RPackageInfo deppackageInfo : depends) {
				boolean ok=runInstallPackageCommand(deppackageInfo,toLib);
				if(!ok)
					return ok;
			}
		}
		return runInstallPackageCommand(packageinfo,toLib);
	}
	
	
	protected static boolean runInstallPackageCommand(RPackageInfo packageinfo,String toLib) throws Exception {
		String pcmd=buildInstallPackageCommand(packageinfo, toLib);
		if(pcmd!=null) {
			if(OSystemUtils.isWindows())
				return runInstallCMD(getSystemR_HOME(),"-e",pcmd);
			else
				return runInstallCMD("R","-e",pcmd);
		}
		return false;
	}
	
	private static String buildInstallPackageCommand(RPackageInfo packageinfo, String toLib) throws Exception{
		
		if(packageinfo.needsDevtools())
			return builtDevtoolsInstallPackageCommand(packageinfo, toLib);
		else {
			StringBuilder str=new StringBuilder();
		
			toLib=validatePath(toLib);

			str.append("install.packages('");
			str.append(packageinfo.getPackageName()+"'");

			if(packageinfo.getPackageCranRepositoryURL()!=null){
				str.append(", repos='");
				str.append(packageinfo.getPackageCranRepositoryURL()+"'");
			}
			if(toLib!=null && !toLib.isEmpty()){
				str.append(", lib='");
				str.append(toLib+"'");
			}

			str.append(", dependencies=c('Depends','Imports','LinkingTo')");
			str.append(")");
		
	
			return str.toString();
		}
	}
	
	private static String builtDevtoolsInstallPackageCommand(RPackageInfo packageinfo, String toLib) throws Exception {
		
		boolean devtoolsok=devtoolsIsInstalled(toLib);
		if(!devtoolsok) {
			
			devtoolsok=installPackage(RPackageInfo.define("devtools"), toLib);
			
		}
		
		if(!devtoolsok)
			return null;
		else {

			String devtoolscmd=null;
			if(toLib!=null)
				devtoolscmd=packageinfo.getDevtoolsInstallCommand(toLib);
			else
				packageinfo.getDevtoolsInstallCommand();
			if(devtoolscmd!=null) {
				String cmd="";
				
				if(toLib!=null)
					cmd=".libPaths('"+toLib+"');";
				cmd=cmd+devtoolscmd;
				return cmd;
			}
			else
				return null;
		}
	}
	
	
	private static boolean devtoolsIsInstalled(String toLib) throws Exception {
		return checkIfPackageIsInstalled(RPackageInfo.define("devtools"), getSystemR_HOME(), toLib);	
	}
	
	public static String validatePath(String path){
		if(path!=null){
			String newpath=null;
			if(OSystemUtils.isWindows()){
				if(path.contains(File.separator))
					newpath=path.replace(File.separator, "/");
				else if(path.contains("\\\\"))
					newpath=path.replace("\\\\", "/");

			}

			if(newpath==null)
				newpath=path;
			return newpath;
		}
		return null;
	}
	
	 
	public static boolean installBioconductorPackage(RPackageInfo packageinfo) throws Exception{
		return installBioconductorPackage(packageinfo, null);
	}
	
	public static boolean installBioconductorPackage(RPackageInfo packageinfo, String toLib) throws Exception{
		ArrayList<String> list=new ArrayList<>();
		list.addAll(packageinfo.getDependenciesPackageNames());
		list.add(packageinfo.getPackageName());
		return installBioconductorPackages(list, toLib);
	}
	
	
	public static boolean installBioconductorPackages(String...packagenames) throws Exception{
		return installBioconductorPackages(null, packagenames);
	}
	
	public static boolean installBioconductorPackages(String toLib, String[] packagenames) throws Exception{
		ArrayList<String> list=new ArrayList<>(Arrays.asList(packagenames));
		return installBioconductorPackages(list, toLib);
	}
	
	
	public static boolean installBioconductorPackages(ArrayList<String> packagenames, String toLib) throws Exception{
		String biocCMD=buildInstallBioconductorPackageCommand(packagenames, toLib);
		if(OSystemUtils.isWindows())
			return runInstallCMD(getSystemR_HOME(),"-e",biocCMD);
		return runInstallCMD("R","-e",biocCMD);
	}
	

	
	private static String buildInstallBioconductorPackageCommand(ArrayList<String> packages,String toLib){
		StringBuilder str=new StringBuilder();

		
		if(toLib!=null)
			str.append(".libPaths('"+toLib+"');");

		str.append(RBIOCONDUCTORSOURCE+";");
		// Certifies that biocLite is installed
		if(toLib!=null)
			str.append("biocLite();");
		else
			str.append(BIOCLITE+";");
		str.append(getBiocLiteCMD(packages, toLib));
		
		return str.toString();
	}
	
	private static String getBiocLiteCMD(ArrayList<String> packages, String toLib){

		StringBuilder str=new StringBuilder();

		
		if(packages!=null && packages.size()>0){
			str.append("biocLite(");
			if(packages.size()==1){
				str.append("'"+packages.get(0)+"'");
			}
			else{
			   
				str.append("c(");
				for (int i = 0; i < packages.size(); i++) {
					str.append("'"+packages.get(i)+"'");
					if(i<(packages.size()-1))
						str.append(", ");
				}
				str.append(")");
			}
			
			if(toLib!=null && !toLib.isEmpty()){
					str.append(",lib.loc ='"+toLib+"', lib='"+toLib+"'");
			}
				
			str.append(", "+NOUPDATESTAG+")");
			
			return str.toString();
			
		}
		return null;
	}
	
	
	
	
	
	public static boolean runInstallCMD(String...cmds) throws Exception{
		return runInstallCMD(true, cmds);
	}
	
	
	public static boolean runInstallCMD(boolean viewoutput,String...cmds) throws Exception{
		ArrayList<String> cmdlist=new ArrayList<>(Arrays.asList(cmds));
		return runInstallCMD(cmdlist,viewoutput);
	}
	
	
	public static boolean runInstallCMD(boolean viewoutput,String rhome,String...cmds) throws Exception{
		ArrayList<String> cmdlist=new ArrayList<>(Arrays.asList(cmds));
		return runInstallCMD(cmdlist, rhome, viewoutput);
	}
	
	
	public static boolean runInstallCMD(AbstractProcessProgressionChecker progressionchecker,String rhome,String...cmds) throws Exception{
		ArrayList<String> cmdlist=new ArrayList<>(Arrays.asList(cmds));
		return runInstallCMD(cmdlist, rhome, progressionchecker);
	}
	
	
	public static boolean runInstallCMD(ArrayList<String> cmds,boolean viewoutput) throws Exception{
		return runInstallCMD(cmds, null, viewoutput);
	}
	
	
	public static boolean runInstallCMD(ArrayList<String> cmds,String rhome,boolean viewoutput) throws Exception{
		if(viewoutput)
			return runInstallCMD(cmds, rhome, new InstallationProgressionChecker());
		else
			return runInstallCMD(cmds, rhome, null);
	}
	
	
	
	
	public static boolean runInstallCMD(ArrayList<String> cmds,String rhome, AbstractProcessProgressionChecker progressionchecker) throws Exception{
		
		boolean sysenvvar=false;
		String R_HOME=null;
		if(rhome!=null){
			R_HOME=rhome;
		}
		else{
			R_HOME=getSystemR_HOME();
			if(R_HOME!=null)
				sysenvvar=true;
		}
		
		LogMessageCenter.getLogger().toClass(RInstallTools.class).addTraceMessage(String.valueOf(cmds));
		ProcessBuilder pb= new ProcessBuilder(cmds);
	
		pb.redirectErrorStream(true);
		if(R_HOME!=null && !R_HOME.isEmpty()){
			Map<String, String> jenv = pb.environment();
			if(R_HOME!=null){
				if(!sysenvvar)
					jenv.put("R_HOME", R_HOME);
				else{
					if(!jenv.containsKey(R_HOME_KEY)){
						if(OSystemUtils.isWindows())
							R_HOME="\""+R_HOME+"\"";
						jenv.put("R_HOME", R_HOME);
					}
				}
			}
		}
		Process p = pb.start();
	
		if(progressionchecker!=null){
			progressionchecker.setInputStream(p.getInputStream());
			Thread stdout=new Thread(progressionchecker);
			stdout.run();
		}
		int exit=p.waitFor();
		if(exit==0)
			return true;
		return false;
	}
	
	
	public static boolean checkIfPackageIsInstalled(RPackageInfo packageinfo,String Rpath,String ruserlibpath ) throws Exception{
		String R_HOME=null;
		
		if(ruserlibpath!=null)
			ruserlibpath=validatePath(ruserlibpath);

		if(Rpath!=null){
			R_HOME=Rpath;
		}
		else{
			R_HOME=getSystemR_HOME();
		}
		

		StringBuilder str=new StringBuilder();
		
		if(OSystemUtils.isWindows())
			str.append("\"");
		else{
			str.append(R_HOME+" ");
			str.append("--vanilla  -e ");
			str.append("'");
		}
		
		if(ruserlibpath!=null){
			if(OSystemUtils.isWindows())
				str.append(".libPaths('"+ruserlibpath+"');");
			else
				str.append(".libPaths(\""+ruserlibpath+"\");");
		}
		
		if(OSystemUtils.isWindows())
			str.append("i=installed.packages();is.element(set=i,el='"+packageinfo.getPackageName()+"')");
		else
			str.append("i=installed.packages();is.element(set=i,el=\""+packageinfo.getPackageName()+"\")");
		
		if(OSystemUtils.isWindows())
			str.append("\"");
		else
			str.append("'");
		
		
		Process p=null;
		
		if(OSystemUtils.isWindows())
			p=Runtime.getRuntime().exec("\""+R_HOME+"\" --vanilla  -e "+str.toString());
		else{
			ArrayList<String> cmd=new ArrayList<>();
			cmd.add("/bin/sh");
			cmd.add("-c");
			cmd.add(str.toString());

			ProcessBuilder pb= new ProcessBuilder(cmd);
			pb.redirectErrorStream(true);
			p=pb.start();
		}


		PackageInstalledChecker checker=new PackageInstalledChecker(p.getInputStream());
		Thread stdout=new Thread(checker);
		stdout.run();

		p.waitFor();
        
		return checker.havePackage();
		
	}
	
	
	
	
	
	
	public static ArrayList<String> checkRLibsFoldersInR(String Rpath) throws Exception{

		String R_HOME=null;
		
	
		if(Rpath!=null){
			R_HOME=Rpath;
		}
		else{
			R_HOME=getSystemR_HOME();
		}
		
		

		StringBuilder str=new StringBuilder();
		
		if(OSystemUtils.isWindows())
			str.append("\"");
		else{
			str.append(R_HOME+" ");
			str.append("--vanilla  -e ");
			str.append("'");
		}
		
		
		str.append(".libPaths();");

		if(OSystemUtils.isWindows())
			str.append("\"");
		else
			str.append("'");
		
		
		Process p=null;
		
		if(OSystemUtils.isWindows())
			p=Runtime.getRuntime().exec("\""+R_HOME+"\" --vanilla  -e "+str.toString());
		else{
			ArrayList<String> cmd=new ArrayList<>();
			cmd.add("/bin/sh");
			cmd.add("-c");
			cmd.add(str.toString());

			ProcessBuilder pb= new ProcessBuilder(cmd);
			pb.redirectErrorStream(true);
			p=pb.start();
		}
		
		RLibsPathChecker checker=new RLibsPathChecker(p.getInputStream());
		Thread stdout=new Thread(checker);
		stdout.run();

		p.waitFor();

		return checker.getRLibs();
		
	}
	
	
	
    public static String getSystemR_HOME() throws Exception {
	        Map<String, String> env = System.getenv();
	        Properties prop = System.getProperties();
	        
	        if (env.containsKey(R_HOME_KEY)) {
	            return env.get(R_HOME_KEY);
	        }
	        else if(prop.containsKey(R_HOME_KEY))
	        	return prop.getProperty(R_HOME_KEY);
	        else{
	        	if(OSystemUtils.isLinux()){
	        		return findArgument("which","R");
	        	}
	        	else if(OSystemUtils.isWindows())
	        		return getWindowRPathFromRegistry();
	        	else
	        		return null;
	        }   
    }
    
    
    public static String getWindowRPathFromRegistry(){
    	
    	
    	
    	String basetree="SOFTWARE\\R-core\\R";
    	
    	String tree=basetree;
    	if(OSystemUtils.getOperationSystem().equals(OS.WIN64))
    		tree="SOFTWARE\\R-core\\R64";

    	try {
    		String Rpath=getRegistryValue(tree);
    		if(Rpath==null)
    			Rpath=getRegistryValue(basetree);

    		if(Rpath!=null){
    			Rpath=Rpath.replace("\\", File.separator)+File.separator+"bin"+File.separator+"R";
    			return Rpath;
    		}
    		
		} catch (RegistryException e) {
			LogMessageCenter.getLogger().addCriticalErrorMessage("Error: ", e);
		}
    	return null;
    }
    
    
    private static String getRegistryValue(String treepath) throws RegistryException{
    	WindowsRegistry regwin=WindowsRegistry.getInstance();
    	
    	List<String> keys = regwin.readStringSubKeys(HKey.HKLM,treepath);
    	String rversion=null;
    	if(keys!=null){
    		if(keys.size()>0)
    			rversion=getHightVersion(keys);
    		else
    			rversion=keys.get(0);
    	}

    	if(rversion!=null){
    		treepath=treepath+"\\"+rversion;
    		String value = regwin.readString(HKey.HKLM, treepath, "InstallPath");
    		String v=FilenameUtils.getBaseName(value);
    		LogMessageCenter.getLogger().addInfoMessage("R install dir: "+value);
    		if(value!=null)
    			return value;
    	}
    	return null;
    }
    
    private static String getHightVersion(List<String> keys){
    	String version=keys.get(0);

		Semver currversion = new Semver(filterVersionNumber(version));
    	
    	for (int i = 1; i < keys.size(); i++) {
    		Semver checkversion =new Semver(filterVersionNumber(keys.get(i)));
			if(checkversion.isGreaterThan(currversion)){
			    version=keys.get(i);
			    currversion=checkversion;
			}
		}
    	
    	return version;
    }
    
    public static String filterVersionNumber(String version){
    	Pattern pat=Pattern.compile("(\\d+(.\\d+)*)");
    	Matcher m=pat.matcher(version);
    	if(m.find()){
    		String vers=m.group(1);
    		return vers;
    	}
    	return version;
    }
    
    public static String getUserSystemRLibs() throws Exception{
    	if(OSystemUtils.isLinux() || OSystemUtils.isLinux()){
    		String r=findArgument("printenv","R_LIBS");
    		if(r!=null && !r.isEmpty())
    			return r;
    	}
    	return null;
    }
    
    
    
    
    public static String findArgument(String...cmds) throws Exception{
    	ProcessBuilder build= new ProcessBuilder(cmds);
    	Process p =build.start();
    	SimpleCMDOutputChecker checker=new SimpleCMDOutputChecker(p.getInputStream());
    	Thread stdout=new Thread(checker);
    	stdout.run();
    	//int exitval=p.waitFor();
    	return checker.getOutput();
    }
	            
	            
	 public static String pandocDirInstallation()  {
	
		if(OSystemUtils.isLinux()) {
			String inst=null;
			try {
				inst = findArgument("which","pandoc");
			} catch (Exception e) {
				LogMessageCenter.getLogger().addErrorMessage(e.getMessage());
			}
			if(inst!=null && !inst.isEmpty())
				return inst;
			else if(new File("/usr/lib/rstudio/bin/pandoc").exists())
				return "/usr/lib/rstudio/bin/pandoc";
		}
		 
		 return null;
	 }
	
	
	

	public static void main(String[] args) throws Exception {
		//System.out.println(RInstallTools.getWindowRPathFromRegistry());
		//System.out.println(RInstallTools.filterVersionNumber("C:\\Program Files\\R\\R-3.4.0patched"));
		/*if(OSystemUtils.isLinux())
			System.out.println(RInstallTools.checkIfPackageIsInstalled("biclust", null, "/home/orocha/discodados/ApenasTrabalho/Testes/newlibs"));
		else
			System.out.println(RInstallTools.checkIfPackageIsInstalled("biclust", null, "C:\\Users\\rocha\\Documents\\testes"));*/
		
		//System.out.println(RInstallTools.checkRLibsFoldersInR(null));
		//System.out.println(getUserSystemRLibs());
		//RInstallTools.installPackage("biclust");
		//RInstallTools.installBioconductorPackage("org.EcK12.eg.db", null);
		//RInstallTools.installBioconductorPackages("/home/orocha/R/x86_64-pc-linux-gnu-library",new String[]{"eisa","fabia"});
        //RInstallTools.runInstallCMD("R", "-e","source(\"https://bioconductor.org/biocLite.R\");biocLite();biocLite(\"org.EcK12.eg.db\")");
		//RInstallTools.runInstallCMD("mate-terminal","-e","R -e 'install.packages(\"biclust\", repos=\"http://cran.us.r-project.org\")'");
		//RInstallTools.runInstallCMD("/bin/sh","-c","R -e 'install.packages(\"biclust\", repos=\"http://cran.us.r-project.org\")'");
		
		ArrayList<String> folderr=checkRLibsFoldersInR(null);
		System.out.println(folderr);
		for (String string : folderr) {
			System.out.println(MTUDirUtils.isFolderWritable(string));
		}
	}
	
	

}
