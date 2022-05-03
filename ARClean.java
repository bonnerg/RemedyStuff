

import java.io.*;
import java.util.*;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import org.apache.commons.io.*;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Constants;
import com.bmc.arsys.api.Entry;
import com.bmc.arsys.api.QualifierInfo;
import com.bmc.arsys.api.Filter;

/**
 * @author George Bonner
 *
 */
public class ARClean {
	public static Logger logger = Logger.getLogger(ARClean.class.getName());
	public static void main(String[] args) throws Exception {
    	PropertyConfigurator.configure("log4j.properties");
		logger.info("Starting ARClean");
		if (args.length != 1) {
			System.out
					.println("Usage: java -jar ARClean <ARClean_Config.xml>");
			System.exit(1);
		}
		
		String configFile = args[0];
		logger.debug("Reading config file: "+configFile);
		File f=new File(configFile);
    	if(! f.isFile()){
    		logger.error("Error " + configFile +" is not a valid file.");
    		System.exit(1);
    	}
    	String server = null;
    	int port = -1;
    	String rUser = null;
    	String rPass = null;
    	
    	String xml = FileUtils.readFileToString(new File(configFile));
    	VTDGen vg = new VTDGen();
		vg.setDoc(xml.getBytes());
		vg.parse(true);
		VTDNav vn = vg.getNav();
		AutoPilot ap0 = new AutoPilot(vn);
		AutoPilot ap1 = new AutoPilot(vn);
		AutoPilot ap2 = new AutoPilot(vn);
		AutoPilot ap3 = new AutoPilot(vn);
		AutoPilot ap4 = new AutoPilot(vn);
		ap0.selectXPath("/ARClean/Connection");
		ap1.selectXPath("ServerName");
		ap2.selectXPath("ServerPort");
		ap3.selectXPath("AdminName");
		ap4.selectXPath("AdminPass");
    	
		while(ap0.evalXPath()!=-1){
			server = ap1.evalXPathToString();
			port = Integer.parseInt(ap2.evalXPathToString());
			rUser = ap3.evalXPathToString();
			rPass = ap4.evalXPathToString();
		}
		

    	logger.debug("Server: "+server);
    	logger.debug("Port: "+port);
    	logger.debug("AdminName: "+rUser);
    	logger.debug("AdminPass: "+rPass);
    	
    	int count = 0;
    	AutoPilot ap = new AutoPilot(vn);
        ap.selectXPath("/ARClean/List/Form");
        while ((ap.evalXPath()) != -1) {
        	count = count +1;
        }
        logger.debug("Forms counted: "+count);
    	
    	String[][] dforms = new String[count-1][2];
    	
    	AutoPilot ap5 = new AutoPilot(vn);
        ap5.selectXPath("/ARClean/List/Form");
        int numForm = 0;
        while ((ap5.evalXPath()) != -1) {
        	int val = vn.getAttrVal("name");
        	if(val!=-1){
        		dforms[numForm][0] = vn.toNormalizedString(val);
        		logger.debug("Form Name: "+vn.toNormalizedString(val));
        	}
        	
        	dforms[numForm][1] = ap5.evalXPathToString();
        	logger.debug("Form Qual: "+ap5.evalXPathToString());
        	numForm ++;
        	vn.toElement(VTDNav.NEXT_SIBLING,"Form");
        }
    	System.exit(0);

		ARServerUser currentUser = new ARServerUser();
		currentUser.setServer(server);
		currentUser.setPort(port);
		currentUser.setUser(rUser);
		currentUser.setPassword(rPass);
		currentUser.setClientType(Constants.AR_CLIENT_TYPE_CACHE);
		int formret[] = new int[1];
		formret[0] = 1;
		String eid = null;

		String[][] dForms = new String[][]{
				{"COM:Company","'Company' != \"- Global -\""},
				{"COM:Company Alias","'Company Alias' != \"- Global -\""},
				{"CTM:SYS-Access Permission Grps","1=1"},
				{"SIT:Site","1=1"},
				{"SIT:Site Alias","1=1"},
				{"SIT:Site Group","1=1"},
				{"SIT:Site Company Association","1=1"},
				{"PCT:Product Catalog","1=1"},
				{"PCT:Product Alias","1=1"},
				{"PCT:ProductCompanyAssociation","1=1"},
				{"PCT:Product Model-Version","1=1"},
				{"PCT:ModelVersion Patch","1=1"},
				{"PCT:ProductCatalogAliasMappingForm","1=1"},
				{"AP:Signature","1=1"},
				{"CFG:Assignment","1=1"},
				{"CFG:Broadcast","1=1"},
				{"CFG:BroadcastSPGAssociation","1=1"},
				{"CFG:Business Holidays Storage","'Tag' != \"24HrsTag\""},
				{"Business Time Workdays","'Tag' != \"24HrsTag\""},
				{"Business Time Segment","1=1"},
				{"CFG:Decision Tree","1=1"},
				{"CFG:Decision Tree-Branch","1=1"},
				{"CFG:Generic Catalog","1=1"},
				{"CFG:GenericCompanyModuleAssoc","1=1"},
				{"CFG:GenericProdServiceAssoc","1=1"},
				{"CFG:GenericCompanyModuleAssoc","1=1"},
				{"CFG:Group Event Mapping","1=1"},
				{"CFG:Reminders","1=1"},
				{"CFG:CFG ScriptTagNumGenerator","1=1"},
				{"CFG:Scripts","1=1"},
				{"CFG:Service Catalog","1=1"},
				{"CFG:Service Catalog Assoc","1=1"},
				{"CHG:Impacted Areas","1=1"},
				{"SRM:AppInstanceBridge","1=1"},
				{"CHG:Template","1=1"},
				{"CHG:TemplateSPGAssoc","1=1"},
				{"CHG:Template Associations","1=1"},
				{"CTM:People Permission Groups","1=1"},
				{"FIN:CostCenterUDAAssociations","1=1"},
				{"CTM:People Template","1=1"},
				{"CTM:People Template PG","1=1"},
				{"AP:Role","1=1"},
				{"CTM:SupportGroupFunctionalRole","1=1"},
				{"CTM:People_Template_SFR","1=1"},
				{"CTM:People Template SG","1=1"},
				{"CTM:Support Group Association","1=1"},
				{"CTM:Region","1=1"},
				{"CTM:Support Group Assignments","1=1"},
				{"CTM:Support Group On-Call","1=1"},
				{"CFG:BusTimeTagGenerator","1=1"},
				{"CTM:Support Group","1=1"},
				{"CTM:Support Group Alias","1=1"},
				{"FIN:Costs","1=1"},
				{"FIN:Association","1=1"},
				{"AST:CMDB Associations","1=1"},
				{"CTR:Contract_Relationship","1=1"},
				{"AST:AssetWarranty_","1=1"},
				{"AST:AssetMaintenance_","1=1"},
				{"AST:AssetSoftware_","1=1"},
				{"AST:AssetSupport_","1=1"},
				{"CTR:GenericContract_","1=1"},
				{"CTR:MasterContract_","1=1"},
				{"AST:AssetLease_","1=1"},
				{"CTR:ContractBase","1=1"},
				{"FIN:ConfigCostCentersRepository","'Company' != \"- Global -\""},
				{"FIN:Payments","1=1"},
				{"HPD:Template","1=1"},
				{"HPD:TemplateSPGAssoc","1=1"},
				{"HPD:Template Associations","1=1"},
				{"NTE:CFG-Pager Service Config","1=1"},
				{"PBM:Impacted Areas","1=1"},
				{"TMS:AssociationTemplate","'Parent Type' != \"Start\""},
				{"TMS:SummaryData","1=1"},
				{"TMS:FlowTemplate","1=1"},
				{"TMS:Task","1=1"},
				{"TMS:Association","1=1"},
				{"TMS:MetricsSummary","1=1"},
				{"TMS:Flow","1=1"},
				{"TMS:TaskGroup","1=1"},
				{"TMS:TaskGroupTemplate","1=1"},
				{"TMS:TaskTemplate","1=1"},
				{"SLM:ConfigSLAOwners","1=1"}
				};
				
		try {
			currentUser.login();
			currentUser.useAdminRpcQueue();
			
			// disable filters to allow deletion
			Filter finNotProposed = currentUser.getFilter("FIN:FCP:CannotDeleteWhenStatusIsNotProposed");
			finNotProposed.setEnable(false);
			currentUser.setFilter(finNotProposed);
			Filter finUnallocated = currentUser.getFilter("FIN:FCP:CannotDeleteUnallocatedCostCenter");
			finUnallocated.setEnable(false);
			currentUser.setFilter(finUnallocated);
						
			// form iteration
			for (int i = 0; i<dForms.length; i++) {
				System.out.println("Deleting from Form: " + dForms[i][0]);
				QualifierInfo qual = currentUser.parseQualification(dForms[i][0], dForms[i][1]);
				List<Entry> ret = currentUser.getListEntryObjects(dForms[i][0], qual, 0, 0, null, formret, false, null);
				if (ret != null) {
					for (Entry thisEntry : ret) {
						eid = thisEntry.get(1).toString();
						System.out.println("Deleting entry: " + eid);
						currentUser.deleteEntry(dForms[i][0],eid,0);
					}
				}
			}
			finNotProposed.setEnable(true);
			finUnallocated.setEnable(true);
			currentUser.setFilter(finNotProposed);
			currentUser.setFilter(finUnallocated);
			currentUser.logout();
						
		} catch (ARException ae) {
			System.out.println(ae.getMessage().toString());
//			log.debug(ae.getMessage().toString());
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
//			log.debug(e.getMessage().toString());
		}
	
	}
    
}
