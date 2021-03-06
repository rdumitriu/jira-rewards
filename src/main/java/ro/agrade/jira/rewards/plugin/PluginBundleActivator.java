/*
 * Created at Sep 16, 2010T4:53:29 PM+02.
 *
 * File: AbstractPluginLifecycleActivator.java
 */
package ro.agrade.jira.rewards.plugin;

import java.util.*;

import org.osgi.framework.*;

import ro.agrade.jira.rewards.dao.*;
import ro.agrade.jira.rewards.services.RewardType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>
 * Our plugins will come to life in init and will die in destroy. OSGI defines
 * a very clear lifecycle, so we can use it to register / unregister our
 * components.
 * </p>
 * <p>
 * Of course, you will have to insert in your manifest entries like:
 *</p>
 * <pre>
 * Bundle-Name: Project name
 * Bundle-Description: Project Description
 * Bundle-Vendor: Agrade
 * Bundle-Version: 1.0
 * Bundle-Activator: ro.agrade.MyPluginActivator
 * Import-Package: org.osgi.framework
 * </pre>
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class PluginBundleActivator implements BundleActivator {
    private static final
    Log LOG = LogFactory.getLog(ro.agrade.jira.rewards.plugin.PluginBundleActivator.class);

    /**
     * Public constructor
     */
    public PluginBundleActivator() {
    }


    /**
     * Starts the bundle
     * @param bundleContext the context (OSGI)
     */
    public void start(BundleContext bundleContext) {
        registerAdditionalDbResources();
        RewardTypeDataService ds = new RewardTypeDataServiceImpl();
        List<RewardType> list = ds.getRewardTypes();
        if(list == null || list.size() == 0) {
            ds.addRewardType(new RewardType(1, "Beer", "Beers", "Drinking habits?", "/download/resources/ro.agrade.jira.rewards:rewards-resources/images/beer_32x32.png"));
        }
    }

    /**
     * Stops the bundle
     * @param bundleContext the context (OSGI)
     */
    public void stop(BundleContext bundleContext) {
    }


    private void registerAdditionalDbResources() {
        LOG.debug("Loading additional Ofbiz resources");
        //we need for sure the config resources. Therefore, we must add them
        GenericDelegatorLoader ufg = new GenericDelegatorLoader("default",
                                                                PluginBundleActivator.class);
		ufg.loadXMLFiles(CFG_LOADER_ENT, CFG_LOADER_ENT_LOCATION,
                         CFG_LOADER_GRP, CFG_LOADER_GRP_LOCATION);
        LOG.debug("Done loading Ofbiz resources.");
    }

    private static final String CFG_LOADER_ENT = "maincp";
	private static final
    String CFG_LOADER_ENT_LOCATION =
            "/ofbiz/entitymodel.xml";
    
	private static final String CFG_LOADER_GRP = "maincp";
	private static final
    String CFG_LOADER_GRP_LOCATION =
            "/ofbiz/entitygroup.xml";
}
