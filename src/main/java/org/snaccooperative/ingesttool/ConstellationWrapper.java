/**
 * SNAC Ingest Tool
 *
 * @author Robbie Hott
 * @license https://opensource.org/licenses/BSD-3-Clause BSD 3-Clause
 * @copyright 2019 the Rector and Visitors of the University of Virginia
 */
package org.snaccooperative.ingesttool;

import org.snaccooperative.data.Constellation;

public class ConstellationWrapper {

    private Constellation constellation;

    private String nameEntry;

    private String status;

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

    public Constellation getConstellation() {
        return constellation;
    }

    public String getNameEntry() {
        return nameEntry;
    }

    public String getStatus() {
        return status;
    }

}
