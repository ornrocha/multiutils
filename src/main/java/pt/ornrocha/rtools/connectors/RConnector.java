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
package pt.ornrocha.rtools.connectors;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.math.R.RserverConf;
import org.math.R.Rsession;
import org.rosuda.REngine.REXPMismatchException;

import pt.ornrocha.logutils.MTULogLevel;
import pt.ornrocha.logutils.messagecomponents.LogMessageCenter;
import pt.ornrocha.rtools.installutils.RInstallTools;
import pt.ornrocha.rtools.installutils.components.RPackageInfo;
import pt.ornrocha.systemutils.OSystemUtils;

public class RConnector {

	public static final String RSERVEPORT="rserve_port";
	public static final String RSERVEHOST="rserve_host";
	static RConnector conn=null;
	private Rsession session;
	private boolean isactive=false;
	public final static String WINSP="'";
	public final static String LINSP="\"";

	//private static String RSERVELOCKFILE="Rserve_on.lock";
	//private static String RSERVELOCKFILEPATH=FilenameUtils.concat(OSystemUtils.getSystemTemporaryFolder(), RSERVELOCKFILE);



	synchronized public static RConnector getCurrentInstance(){
		return conn;
	}

	synchronized public static RConnector startLocalInstance(){
		return startInstance(null,null, null);
	}

	synchronized public static RConnector startLocalInstance(String Ruserlibspath){
		return startInstance(Ruserlibspath,null, null);
	}

	synchronized public static RConnector startInstance(String R_user_lib, String host,String port){	
		if(conn==null)
			conn=new RConnector(R_user_lib,host,port,null, null);
		return conn;
	}

	synchronized public static RConnector startInstance(RConnectorProperties rcprops){
		if(conn==null){
			conn=new RConnector(rcprops.getRUserFolderPath(), rcprops.getHost(), rcprops.getPort(), rcprops.getUsername(), rcprops.getPassword());
		}
		return conn;
	}



	private RConnector(String R_user_lib, String host, String port, String username, String password){

		RserverConf conf=null;
		if(host!=null || port!=null){
			int rsport=6311;
			try {
				if(port!=null)
					rsport=Integer.parseInt(port);
			} catch (Exception e) {
				rsport=6311;
			}

			if(username!=null && password!=null)
				conf=new RserverConf(host, rsport, username, password, null);
			else
				conf=new RserverConf(host, rsport, null, null, null);
		}

		if(host!=null && (!host.toLowerCase().equals("localhost") || !host.equals("127.0.0.1")))
			this.session=Rsession.newRemoteInstance(LogMessageCenter.getLogger(), conf);
		else if(conf!=null){
			if(R_user_lib!=null)
				this.session=Rsession.newInstanceTry(LogMessageCenter.getLogger(), conf, R_user_lib);
			else
				this.session=Rsession.newInstanceTry(LogMessageCenter.getLogger(), conf);
		}
		else{
			conf=new RserverConf("127.0.0.1", 0, null, null, null);
			if(R_user_lib!=null)
				this.session=Rsession.newInstanceTry(LogMessageCenter.getLogger(), conf, R_user_lib);
			else
				this.session=Rsession.newInstanceTry(LogMessageCenter.getLogger(), conf);
		}
		if(session!=null)
			isactive=true;
	}


	/*private RConnector(String host, String port, String username, String password){

		RserverConf conf=null;
		if(host!=null || port!=null){
			int rsport=6311;
			try {
				if(port!=null)
					rsport=Integer.parseInt(port);
			} catch (Exception e) {
				rsport=6311;
			}

			if(username!=null && password!=null)
				conf=new RserverConf(host, rsport, username, password, null);
			else
				conf=new RserverConf(host, rsport, null, null, null);
		}

	    if(host!=null && (!host.toLowerCase().equals("localhost") || !host.equals("127.0.0.1")))
			this.session=RsessionMod.newRemoteInstance(LogMessageCenter.getLogger(), conf);
		else if(conf!=null)
		    this.session=RsessionMod.newInstanceTry(LogMessageCenter.getLogger(), conf);
		else{
			conf=new RserverConf("127.0.0.1", 0, null, null, null);
			this.session=RsessionMod.newInstanceTry(LogMessageCenter.getLogger(), conf);
		}
		if(session!=null)
			isactive=true;
	}*/


	public synchronized static Rsession getExclusiveRsession(String R_user_libs, String host,int port){
		RserverConf conf=null;
		if(host!=null){
			conf=new RserverConf(host, port, null, null, null);

			if((!host.toLowerCase().equals("localhost") || !host.equals("127.0.0.1")))
				return Rsession.newRemoteInstance(LogMessageCenter.getLogger(), conf);
			else{
				if(R_user_libs!=null)
					return Rsession.newInstanceTry(LogMessageCenter.getLogger(), conf, R_user_libs);
				else
					return Rsession.newInstanceTry(LogMessageCenter.getLogger(), conf); 
			}
		}
		else{
			conf=new RserverConf("127.0.0.1", port, null, null, null);
			if(R_user_libs!=null)
				return Rsession.newInstanceTry(LogMessageCenter.getLogger(), conf, R_user_libs);
			else
				return Rsession.newInstanceTry(LogMessageCenter.getLogger(), conf);
		}

	}


	/*public RsessionMod getCurrentSession(){
		return session;
	}*/


	public static boolean isSessionOn(){
		if(getCurrentInstance()!=null && getCurrentInstance().isactive)
			return true;
		return false;
	}


	public static Rsession getSession(){
		if(conn!=null && conn.session!=null)
			return conn.session;
		return null;
	}

	public static Rsession getSessionTryMode(){
		return startLocalInstance().session;
	}


	public static void closeSession(){
		if(isSessionOn() && conn!=null && conn.session!=null){
			conn.session.end();
			conn.isactive=false;
		}
	}

	public static boolean loadRPackageInCurrentSession(RPackageInfo packageinfo){
		if(getSession()!=null)
			return loadRPackage(getSession(),packageinfo);
		return false;
	}

	public synchronized static boolean loadRPackageInCurrentSession(RPackageInfo packageinfo, String RLib){
		if(getSession()!=null)
			return loadRPackage(getSession(), packageinfo, RLib);
		return false;
	}



	public synchronized static Rsession getNewLocalRsession(){
		return Rsession.newInstanceTry(LogMessageCenter.getLogger(), null);
	}

	public synchronized static Rsession getNewLocalRsession(String R_user_libs, String host,int port){
		RserverConf conf=null;
		if(host!=null){
			conf=new RserverConf(host, port, null, null, null);
		}
		return Rsession.newInstanceTry(LogMessageCenter.getLogger(), conf,R_user_libs);
	}




	public synchronized static void setRUserLibPath(Rsession rsession, String path){
		String sep=LINSP;
		if(OSystemUtils.isWindows())
			sep=WINSP;

		if(OSystemUtils.isWindows() && path.contains(File.separator))
			path=path.replace(File.separator, "/");

		rsession.eval(".libPaths("+sep+path+sep+")");

	}



	public static boolean loadRPackage(Rsession rsession, RPackageInfo packageinfo){
		return loadLibrary(rsession,packageinfo,null);
	}

	public static boolean loadRPackage(Rsession rsession,RPackageInfo packageinfo,String ruserLib){
		return loadLibrary(rsession, packageinfo,ruserLib);
	}

	protected synchronized static boolean loadLibrary(Rsession rsession, RPackageInfo packageinfo,String Ruserlibpath){
		//System.out.println("Input Rlib: "+RLib);
		try {
			boolean ok = loadLibraryInRsession(rsession, packageinfo,Ruserlibpath);

			if(!ok){

				boolean installed=RInstallTools.checkIfPackageIsInstalled(packageinfo, null, Ruserlibpath);

				if(!installed){
					LogMessageCenter.getLogger().toClass(RConnector.class).addInfoMessage("Library ["+packageinfo.getPackageName()+"] is not installed, trying automatic installation...");
					if(Ruserlibpath!=null && !Ruserlibpath.isEmpty()){
						if(packageinfo.isBioconductorPackage())
							installed=RInstallTools.installBioconductorPackage(packageinfo, Ruserlibpath);
						else
							installed=RInstallTools.installPackage(packageinfo, Ruserlibpath);

					}
					else{
						if(packageinfo.isBioconductorPackage())
							installed=RInstallTools.installBioconductorPackage(packageinfo);
						else
							RInstallTools.installPackage(packageinfo);

					}


					if(!installed){
						LogMessageCenter.getLogger().toClass(RConnector.class).addCriticalErrorMessage("The library ["+packageinfo.getPackageName()+"] was not found at the current R environment\n"+
								"Please if it is a package of bioconductor, perform the installation: \n"+
								" source(\"https://bioconductor.org/biocLite.R\"); biocLite(\"package_name\");\n"+
								" If it is a R package, install in R environment by doing: \n"+
								" install.packages(\"package_name\") \n");
						return false;
					}
				}

				if(installed){

					ok=loadLibraryInRsession(rsession,packageinfo,Ruserlibpath);

					if(ok){
						LogMessageCenter.getLogger().toClass(RConnector.class).addInfoMessage("Library ["+packageinfo.getPackageName()+"] was successfully installed, and was loaded.");
						return ok;
					}
					else{
						LogMessageCenter.getLogger().toClass(RConnector.class).addCriticalErrorMessage("The library ["+packageinfo.getPackageName()+"] was not found at the current R environment\n"+
								"Please if it is a package of bioconductor, perform the installation: \n"+
								" source(\"https://bioconductor.org/biocLite.R\"); biocLite(\"package_name\");\n"+
								" If it is a R package, install in R environment by doing: \n"+
								" install.packages(\"package_name\") \n");
						return false;
					}
				}
				else{
					LogMessageCenter.getLogger().toClass(RConnector.class).addCriticalErrorMessage("The library ["+packageinfo.getPackageName()+"] was not found at the current R environment\n"+
							"Please if it is a package of bioconductor, perform the installation: \n"+
							" source(\"https://bioconductor.org/biocLite.R\"); biocLite(\"package_name\");\n"+
							" If it is a R package, install in R environment by doing: \n"+
							" install.packages(\"package_name\") \n");
					return false;
				}
			}
			else
				return ok;

		} catch (Exception e) {
			LogMessageCenter.getLogger().toClass(RConnector.class).addErrorMessage("The library ["+packageinfo.getPackageName()+"] cannot be loaded");
			LogMessageCenter.getLogger().toClass(RConnector.class).addCriticalErrorMessage("The library ["+packageinfo.getPackageName()+"] was not found at the current R environment\n"+
					"Please if it is a package of bioconductor, perform the installation: \n"+
					" source(\"https://bioconductor.org/biocLite.R\"); biocLite(\"package_name\");\n"+
					" If it is a R package, install in R environment by doing: \n"+
					" install.packages(\"package_name\") \n");
			return false;
		}
	}




	private synchronized static boolean loadLibraryInRsession(Rsession rsession, RPackageInfo packageinfo,String Ruserlibspath){

		if(rsession.R_USER_LIBS==null && Ruserlibspath!=null)
			setRUserLibPath(rsession, Ruserlibspath);



		if(rsession.isPackageLoaded(packageinfo.getPackageName())){
			return true;
		}
		else{
			String loaded=rsession.loadPackage(packageinfo.getPackageName());


			if(loaded.equals(Rsession.PACKAGELOADED)){
				LogMessageCenter.getLogger().toClass(RConnector.class).addInfoMessage("Library ["+packageinfo.getPackageName()+"] was loaded successfully.");
				return true;
			}
			else{
				LogMessageCenter.getLogger().toClass(RConnector.class).addInfoMessage("Library ["+packageinfo.getPackageName()+"] was unable of loading...");
				return false;
			}

		}
	}



	public static ArrayList<String> getRLibPaths(Rsession rsession) throws REXPMismatchException{
		String[] res=rsession.eval(".libPaths()").asStrings();
		if(res!=null)
			return new ArrayList<>(Arrays.asList(res));		
		return new ArrayList<>();
	}




	public static boolean loadRequiredLibraries(Rsession rsession, ArrayList<RPackageInfo> requiredlibs) throws Exception{
		return loadRequiredLibraries(rsession,requiredlibs, null);
	}


	public static boolean loadRequiredLibraries(Rsession rsession, ArrayList<RPackageInfo> requiredlibs, String RLibPath) throws Exception{


		boolean loadok=true;

		if(requiredlibs!=null){
			for (RPackageInfo packageInfo : requiredlibs) {

				if(RLibPath!=null && !RLibPath.isEmpty())
					loadok=RConnector.loadRPackage(rsession, packageInfo,RLibPath);
				else
					loadok=RConnector.loadRPackage(rsession, packageInfo);

				if(!loadok)
					return false;
			}
		}
		return loadok;
	}	



	public static String getRequiredPackagesToString(ArrayList<RPackageInfo> reqpackages) {
		StringBuilder str=new StringBuilder();
		for (int i = 0; i < reqpackages.size(); i++) {
			str.append(reqpackages.get(i).getPackageName()+" ");
		}
		return str.toString();
	}
	
	
	public static String getRequiredSourcesToString(ArrayList<String> sources) {
		StringBuilder str=new StringBuilder();
		for (int i = 0; i < sources.size(); i++) {
			str.append(sources.get(i)+" ");
		}
		return str.toString();
		
	}



	public static void main(String[] args) throws Exception {
		//RConnector.getInstance().loadBioconductorLibrary("fabia");
		//RConnector.getInstance().loadRLibrary("isa2");
		LogMessageCenter.getLogger().setLogLevel(MTULogLevel.TRACE);
		RConnector.startInstance(null,null, null);
		//String lib="/home/orocha/discodados/RLibs";
		//RConnectorNew.setRLibPath(lib);
		//loadRLibrary("isa2", lib);
		//RConnectorNew.getSession().end();
		//loadRLibrary("isa2");
	}


}
