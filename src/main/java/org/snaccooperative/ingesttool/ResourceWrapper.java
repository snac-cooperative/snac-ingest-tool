/**
 * SNAC Ingest Tool
 *
 * @author Robbie Hott
 * @license https://opensource.org/licenses/BSD-3-Clause BSD 3-Clause
 * @copyright 2019 the Rector and Visitors of the University of Virginia
 */
package org.snaccooperative.ingesttool;

import org.snaccooperative.data.Resource;

import java.util.Objects;

public class ResourceWrapper {


    private Resource resource;

    private String title;

    private String status;

    private String uploadStatus;

    private String serverResponse;

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

    public void setResource(Resource r, String status) {
        setResource(r);
        this.status = status;
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

    public void setStatus(String status) {
        this.status = status;
    }
    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(String serverResponse) {
        this.serverResponse = serverResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceWrapper that = (ResourceWrapper) o;
        return resource.equals(that.resource);
    }

    @Override
    public int hashCode() {

        return resource.hashCode();
    }
}
