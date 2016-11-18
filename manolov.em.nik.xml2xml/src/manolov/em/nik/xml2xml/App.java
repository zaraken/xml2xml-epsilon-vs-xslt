package manolov.em.nik.xml2xml;

import java.io.File;

import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;
import org.eclipse.epsilon.etl.EtlModule;

public class App {

	public static void main(String[] args) throws Exception {
		if (args.length == 4){ // params passed
			new App().execute(args[0], args[1], args[2]);
		} else {
			new App().execute("src\\manolov\\em\\nik\\xml2xhtml\\library2index.etl"
					, "src\\manolov\\em\\nik\\xml2xhtml\\library.xml"
					, "src\\manolov\\em\\nik\\xml2xhtml\\index.xml");
		}
		
	}
	
	protected void execute(String etlScript, String sourceXml, String targetXml) throws Exception {
		
		// standard boilerplate for setup and execution of transformation
		
		EtlModule module = new EtlModule();
		
		PlainXmlModel source = new PlainXmlModel();
		source.setName("Source"); // name used by etl script
		source.setFile(new File(sourceXml));
		source.setReadOnLoad(true); // read the contents of the file
		source.setStoredOnDisposal(false); // do not save changes
		source.load();
		
		PlainXmlModel target = new PlainXmlModel();
		target.setName("Target"); // name used by etl script
		target.setFile(new File(targetXml));
		target.setReadOnLoad(false); // don't read contents of file
		target.setStoredOnDisposal(true); // save changes
								// this effectively overwrites the file
		target.load();
		
		module.parse(new File(etlScript)); // load transformation script
		module.getContext().getModelRepository().addModel(source);
		module.getContext().getModelRepository().addModel(target);
		
		module.execute();
		
		module.getContext().getModelRepository().dispose();
		module.getContext().dispose();
	}

}
