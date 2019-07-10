/**
 * SNAC Ingest Tool
 *
 * @author Robbie Hott
 * @license https://opensource.org/licenses/BSD-3-Clause BSD 3-Clause
 * @copyright 2019 the Rector and Visitors of the University of Virginia
 */
package org.snaccooperative.ingesttool;

import org.snaccooperative.data.Resource;

public class ResourceWrapper {


    private Resource resource;

    private String title;

    private String status;

    public ResourceWrapper(Resource c) {
        setResource(c);
    }

    public void setResource(Resource c) {
        resource = c;
        if (c.getDisplayEntry() != null)
            title = c.getDisplayEntry();
        else
            title = c.getTitle();

        if (c.getID() == 0)
            status = "New";
        else
            status = "Update ("+c.getID()+")";
    }

    public Resource getResource() {
        return resource;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

}
