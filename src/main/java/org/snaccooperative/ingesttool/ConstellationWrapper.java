/**
 * SNAC Ingest Tool
 *
 * @author Robbie Hott
 * @license https://opensource.org/licenses/BSD-3-Clause BSD 3-Clause
 * @copyright 2019 the Rector and Visitors of the University of Virginia
 */
package org.snaccooperative.ingesttool;

import org.snaccooperative.data.Constellation;

import java.util.Objects;

public class ConstellationWrapper {

    private Constellation constellation;

    private String nameEntry;

    private String status;


    private String uploadStatus;

    private String serverResponse;

    public ConstellationWrapper(Constellation c) {
        setConstellation(c);
    }

    public void setConstellation(Constellation c) {
        constellation = c;
        nameEntry = c.getPreferredNameEntry().getOriginal();
        if (c.getID() == 0)
            status = "New";
        else
            status = "Update ("+c.getID()+")";
    }

    public void setConstellation(Constellation c, String status) {
        setConstellation(c);
        this.status = status;
    }

    public Constellation getConstellation() {
        return constellation;
    }

    public String getNameEntry() {
        return nameEntry;
    }

    public String getStatus() {
        return status;
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
        ConstellationWrapper that = (ConstellationWrapper) o;
        return Objects.equals(constellation, that.constellation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(constellation);
    }
}
