import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path
import java.text.SimpleDateFormat;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import java.io.IOException;
import java.lang.Boolean;

println "Leyendo el fichero properties..." 
Properties prop = leeProperties();

String patronEncriptablePrincipal = prop.prefijoEncriptables + prop.patronPrincipal
String patronEncriptables = prop.prefijoEncriptables + "*.*";
String ultimoBackup = prop.dirBackup + getFirstFile(prop.dirBackup,prop.patronBackup)
String ubicacionVersiones = getThisDirectory() + prop.dirVersiones

String ubicacionBackup = prop.dirBackup

println "Creando el .7z..." 
try{
	String nombreDoc = getFirstFile(getThisDirectory(),patronEncriptablePrincipal);	
	String nombre7z = changeFileExtensionTo(nombreDoc, "7z")
	String archivo7z = getThisDirectory() + "/" + nombre7z
	encriptaEnDosPasos(nombre7z,patronEncriptables);

	try {		
		println "Moviendo ultimo backup al directorio historico de versiones..." 
		moveFile(ultimoBackup, ubicacionVersiones)
		
		println "Moviendo el archivo recien encriptado al directorio de backup..." 
		moveFile(archivo7z, ubicacionBackup)
		
	  	println "++++++++++++++++++  Backup terminado  +++++++++++++++++++++++" 
		println  ""
		
	} catch (IOException e) {
		println ">>>> No se ha movido el .7z generado; existe ya en destino? <<<<" 
	}
	
} catch (Exception e) {
	println "ERROR: Ha ocurrido algo antes de encriptar"	
	e.printStackTrace()
}

esperaHastaQueTeLoDigan()

//-------------------------------------------------------------------

	Properties leeProperties(){
		Properties prop = new Properties()
		File propertiesFile = new File(getThisDirectory()+"/utilities/backup.properties")
		propertiesFile.withInputStream {
			prop.load(it)
		}
		return prop;
	}
	
	void encriptaEnDosPasos(nombre7z,patronEncriptables){
		Properties prop = leeProperties()
		encripta("archivointerno.7z",patronEncriptables,prop.passwordInterno)
		encripta(nombre7z,"archivointerno.7z",prop.passwordExterno)
		deleteFile("archivointerno.7z")
	}
	
	void encripta(nombre7z,patronEncriptables,password){
		try{
			def proc = ('7z a '+nombre7z +' '+ patronEncriptables +' -p'+password).execute();
			proc.waitForOrKill(1000);
		} catch (Exception e) {
			println "ERROR: No se ha podido encriptar el archivo "+nombre7z
		}
	}
	
	void moveFile(origen,destino){
		File srcBackup = new File(origen);
		File destDirVersiones = new File(destino);
		FileUtils.moveFileToDirectory(srcBackup, destDirVersiones, false);
	}
	
	void deleteFile(file){
		try{
			File paraBorrar = new File(getThisDirectory() + "/" + file)
			paraBorrar.delete();
		} catch (Exception e){
			println ">>>>>>>  No se ha podido borrar "+file+" <<<<<<<<<<<<"
		}
	}

	String getFirstFile(directorio, patron){
		FileFilter fileFilter = new WildcardFileFilter(patron);
		File dirActual = new File(directorio);
		File[] files = FileUtils.listFiles(dirActual, fileFilter, null);
		return files[0].getName();
	}

	String getThisDirectory(){
		return new File(getClass().protectionDomain.codeSource.location.path).parent
	}

	String changeFileExtensionTo(nombreDoc, ext){
		return nombreDoc.substring(2,nombreDoc.indexOf('.'))+'.'+ ext
	}

	void esperaHastaQueTeLoDigan(){
		println "Pulsa para salir..." 
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
		def userInput = br.readLine()
	}


//SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//String date = format.format(new Date()); 