package com.mngi.dashboard.contentmanagement.formhandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.ServletException;

import atg.droplet.DropletException;
import atg.droplet.GenericFormHandler;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import atg.userprofiling.MngiProfile;

import com.mngi.openframework.util.DynamicBean;
import com.mngi.openframework.util.date.DateTimeHelper;
import com.mngi.repository.DynamicRepositoryItemComparator;
import com.mngi.repository.QueryManager;

public class SearchFormHandler extends GenericFormHandler {

    // ///////////////////////////////////////////////////

    public void logError(String msg) {
        addFormException(new DropletException(msg));
    }

    public String getResetFormExceptions() {
        // fake access from .jsp
        this.resetFormExceptions();
        return "";
    }

    private void initializeDefaultValues(Map value) {
        String[] defaultValues = getDefaultValues();
        for (int x = 0; x < defaultValues.length; x++) {
            StringTokenizer tokenizer = new StringTokenizer(defaultValues[x],
                    "=", false);
            int numOfTokens = tokenizer.countTokens();
            switch (numOfTokens) {
                case 1:
                    value.put(tokenizer.nextToken(), "");
                    break;
                case 2:
                    value.put(tokenizer.nextToken(), tokenizer.nextToken());
                    break;
                default:
                    logError(new StringBuffer(
                            "ERROR: SearchFormHandler.initializeDefaultValues(Map ")
                            .append(value)
                            .append("), new StringTokenizer(( defaultValues[")
                            .append(x)
                            .append(
                                    "],\"=\",false ) did NOT tokenize correctly,")
                            .append("should be of the syntax: key=value, ")
                            .append(numOfTokens)
                            .append(
                                    " tokens were detected.  Check the defaultValues")
                            .append("variable in your properties file.")
                            .toString());
                    break;
            }
        }
    }

    protected String addDateToQuery(String query, String piece, String key)
            throws Exception {
        if (isLoggingDebug()) {
            // logDebug("BEGIN: addDateToQuery(" + query + ", " + piece + ", " +
            // key + ")");
        }
        Date value = null;
        Object oValue = get(key);
        if ((oValue != null) && !(oValue instanceof String)) {
            value = (Date) oValue;
        }

        if (value != null) {
            if ("".equals(query)) {
                query += piece + "?" + getNextParamNum(value);
            } else {
                query += " AND " + piece + "?" + getNextParamNum(value);
            }
        }
        if (isLoggingDebug()) {
            // logDebug("END: addToQuery(" + query + ", " + piece + ", " + key +
            // ")");
        }
        return query;
    }

    protected String addToQuery(String query, String piece, String key)
            throws Exception {
        if (isLoggingDebug()) {
            // logDebug("BEGIN: addToQuery(" + query + ", " + piece + ", " + key
            // + ")");
        }
        String value = "";
        Object oValue = get(key);
        if (oValue != null) {
            value = get(key).toString();
        }

        if (value != null && !"".equals(value)) {
            if ("".equals(query)) {
                query += piece + "?" + getNextParamNum(value);
            } else {
                query += " AND " + piece + "?" + getNextParamNum(value);
            }
        }
        if (isLoggingDebug()) {
            // logDebug("END: addToQuery(" + query + ", " + piece + ", " + key +
            // ")");
        }
        return query;
    }

    protected String addSplitQuery(String query, String piece, String key)
            throws Exception {

        if (isLoggingDebug()) {
            // logDebug("BEGIN: addSplitQuery(" + query + ", " + piece + ", " +
            // key + ")");
        }
        String value = "";
        Object oValue = get(key);
        if (oValue != null) {
            value = get(key).toString();
        }

        if (value != null && !"".equals(value)) {
            StringTokenizer tokenizer = new StringTokenizer(value);
            if (tokenizer.countTokens() > 0) {
                query += " AND (";
                int counter = 0;
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (counter++ == 0) {
                        query += piece + "?" + getNextParamNum(token);
                    } else {
                        query += " OR " + piece + "?" + getNextParamNum(token);
                    }
                }
                query += ")";
            }
        }
        if (isLoggingDebug()) {
            // logDebug("END: addSplitQuery(" + query + ", " + piece + ", " +
            // key + ")");
        }
        return query;
    }

    public int getNextParamNum(Object object) {
        int currentParamNum = numParams;
        if (numParams < MAX_PARAMS) {
            params[numParams] = object;
            numParams++;
        } else {
            logError("The maximum number of parameters [" + MAX_PARAMS
                    + "] has been exceeded...");
        }
        return currentParamNum;
    }

    public Object[] getParams() {
        Object[] result = new Object[numParams];
        for (int x = 0; x < result.length; x++) {
            result[x] = params[x];
        }
        return result;
    }

    public void resetParameters() {
        params = new Object[MAX_PARAMS];
        numParams = 0;
    }

    public String listParams() {
        StringBuffer buffer = new StringBuffer();
        Object[] params = getParams();
        buffer.append(params.length).append(" parameters");
        for (int x = 0; x < params.length; x++) {
            buffer.append(", ?").append(x).append(" = ").append(params[x]);
        }
        return buffer.toString();
    }

    public String addIdExclusionQuery(String query, String itemType) {
        String[] ids = null;
        String temp = this.getExcludeIds();
        if (isLoggingDebug()) {
            logDebug("addIdExclusionQuery, temp = " + temp);
        }
        String queryIds = "";
        if (temp != null && !"".equals(temp)) {
            if (temp.indexOf(",") != -1) {
                ids = temp.split(",");
            } else {
                ids = new String[1];
                ids[0] = temp;
            }
            boolean first = true;
            for (int x = 0; x < ids.length; x++) {
                String[] parts = ids[x].split(":");
                if (itemType.equalsIgnoreCase(parts[0])) {
                    if (first == true) {
                        queryIds += "\"" + parts[1] + "\"";
                        first = false;
                    } else {
                        queryIds += ",\"" + parts[1] + "\"";
                    }
                }
            }
        }
        if (!"".equals(queryIds)) {
            query += " AND NOT ID IN {" + queryIds + "}";

        }
        return query;
    }

    public String addStateQuery(String query) {
        String state = (String) get("state");
        if (isLoggingDebug()) {
            logDebug("addStateQuery, state = " + state);
        }

        Map states = new HashMap();

        if (state == null || "".equals(state)) {

        } else {
            states.put(state, state);
        }

        // include deleted items:
        boolean includeDeleted = new Boolean((String) get("includeDeleted"))
                .booleanValue();
        if (includeDeleted) {
            states.put("DELETED", "DELETED");
        } else {

        }

        boolean empty = true;
        String stateQuery = " AND (";
        Set keys = states.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String stateId = (String) it.next();
            if (empty) {
                stateQuery += "StateId = \"" + stateId + "\"";
                empty = false;
            } else {
                stateQuery += " OR StateId = \"" + stateId + "\"";
            }
        }
        stateQuery += ")";

        if (!empty) {
            query += stateQuery;
        }

        return query;
    }

    /*
     * Keep this, but we're rolling back to the previous way to represent STATE
     * in the query. for now. -JDA 07-24-06
     * 
     * public String addStateQuery(String query) { String state = (String)
     * get("state"); if (isLoggingDebug()){ logDebug("addStateQuery, state = " +
     * state); } Map states = new HashMap();
     * 
     * if (state == null || "".equals(state)) { states.put("NEWAUTHORED",
     * "NEWAUTHORED"); states.put("APPROVED", "APPROVED");
     * states.put("PRODDEPLOY", "PRODDEPLOY"); } else { states.put(state,
     * state); }
     *  // include deleted items: boolean includeDeleted = new
     * Boolean((String)get("includeDeleted")).booleanValue(); if
     * (includeDeleted) { states.put("DELETED", "DELETED"); }
     *  // include expired items: boolean includeExpired = new
     * Boolean((String)get("includeExpired")).booleanValue(); if
     * (includeExpired) { states.put("EXPIRED", "EXPIRED"); }
     * 
     * boolean empty = true; String stateQuery = " AND ("; Set keys =
     * states.keySet(); Iterator it = keys.iterator(); while (it.hasNext()) {
     * String stateId = (String) it.next(); if (empty) { stateQuery += "StateId =
     * \"" + stateId + "\""; empty = false; } else { stateQuery += " OR StateId =
     * \"" + stateId + "\""; } } stateQuery += ")";
     * 
     * if (!empty) { query += stateQuery; }
     * 
     * return query; }
     */

    // ///////////
    // filters //
    // ///////////
    /*
     * public RepositoryItem[] getFilteredResults() { RepositoryItem[]
     * queryResults = (RepositoryItem[]) get("searchResults");
     * 
     * RepositoryItem[] filteredResults = filterByGroups(queryResults);
     * 
     * return filteredResults; }
     */

    public RepositoryItem[] filterByGroups(RepositoryItem[] items, String allGroups) {
        // allGroups example: 28|Business~81|Classifieds
        Set filterGroupIds = new HashSet();
        if (allGroups != null && !"".equals(allGroups)) {
            String[] groups = allGroups.split("~");
            if (groups != null) {
                for (int x = 0; x < groups.length; x++) {
                    String group = groups[x];
                    String[] parts = group.split("\\|");
                    if (parts != null) {
                        String groupId = parts[0];
                        filterGroupIds.add(groupId);
                    }
                }

                // we have all groupId's in a set, filter the results:
                Set finalSet = new HashSet();
                for (int x = 0; x < items.length; x++) {
                    RepositoryItem item = items[x];
                    Set cicgSet = (Set) item
                            .getPropertyValue("ContentItemContentGroups");
                    if (cicgSet != null && cicgSet.size() > 0) {
                        Iterator it = cicgSet.iterator();
                        boolean noMatch = true;
                        while (noMatch && it.hasNext()) {
                            RepositoryItem cicg = (RepositoryItem) it.next();
                            String groupId = (String) cicg
                                    .getPropertyValue("ContentGroupId");
                            if (filterGroupIds.contains(groupId)) {
                                finalSet.add(item);
                                noMatch = false;
                                if (isLoggingDebug()) {
                                    logDebug("!! filterByGroups matched: item # " + x
                                            + ", it had a cicg with groupId = "
                                            + groupId);

                                }
                            }
                        }
                    }
                }
                if (isLoggingDebug()) {
                    logDebug("!! filterByGroups found " + finalSet.size()
                            + " matches out of " + items.length
                            + " possibles, using cicg's with groupIds in "
                            + filterGroupIds);
                }
                RepositoryItem[] tempArray = new RepositoryItem[finalSet.size()];
                items = (RepositoryItem[]) finalSet.toArray(tempArray);

            }
        }
        return items;
    }

    public String addGroupsToQuery(String query) {
        // 79|Automotive~28|Business~1848122|Cars
        String allGroups = (String) get("selectedContentGroups");
        if (isLoggingDebug()) {
            logDebug("..addGroupsToQuery, allGroups = " + allGroups);
        }
        if (allGroups != null && !"".equals(allGroups)) {
            String[] groups = allGroups.split("~");
            // 79|Automotive
            // 28|Business
            // 1848122|Cars
            if (groups != null) {
                String groupQuery = " AND (";
                boolean empty = true;
                for (int x = 0; x < groups.length; x++) {
                    String group = groups[x];
                    if (isLoggingDebug()) {
                        logDebug("..addGroupsToQuery, group = " + group);
                    }
                    String[] parts = group.split("\\|");
                    if (parts != null) {
                        if (isLoggingDebug()) {
                            logDebug("..addGroupsToQuery, parts.length = "
                                    + parts.length);
                        }
                        if (empty) {
                            // groupQuery += "\"" + parts[0] + "\"";
                            groupQuery += "ContentItemContentGroups.ContentGroupId = \""
                                    + parts[0] + "\"";
                            empty = false;
                        } else {
                            // groupQuery += ",\"" + parts[0] + "\"";
                            groupQuery += " OR ContentItemContentGroups.ContentGroupId = \""
                                    + parts[0] + "\"";
                        }
                    }
                }
                // query += groupQuery + ")";

            }
        }
        return query;
    }

    public boolean handleSearch(DynamoHttpServletRequest pRequest,
            DynamoHttpServletResponse pResponse) throws IOException,
            ServletException {
        if (isLoggingDebug()) {
            logDebug("   ");
            logDebug("BEGIN: handleSearch...");
        }

        set("previouslySubmitted", "true");

        return checkFormRedirect(get("successUrl").toString(), get("errorUrl")
                .toString(), pRequest, pResponse);
    }

    public boolean handleReset(DynamoHttpServletRequest pRequest,
            DynamoHttpServletResponse pResponse) throws IOException,
            ServletException {
        this.reset();
        return checkFormRedirect(get("successUrl").toString(), get("errorUrl")
                .toString(), pRequest, pResponse);
    }

    public boolean handleConfirmDeleteItems(DynamoHttpServletRequest pRequest,
            DynamoHttpServletResponse pResponse) throws IOException,
            ServletException {
        if (isLoggingDebug()) {
            logDebug("in handleConfirmDeleteItems... getSelectedResults = "
                    + get("selectedResults"));
        }
        return checkFormRedirect(get("confirmDeleteSuccessUrl").toString(),
                get("errorUrl").toString(), pRequest, pResponse);
    }

    /**
     * This method handles the cancelation of the deletion of items. It simply
     * redirects to the cancel delete page.
     * 
     * @param pRequest
     *            Description of Parameter
     * @param pResponse
     *            Description of Parameter
     * @return boolean True if form processing should continue, false if it
     *         should be aborted.
     * @throws ServletException,
     *             IOException
     * @exception ServletException
     *                Description of Exception
     * @exception IOException
     *                Description of Exception
     */
    public boolean handleCancelDeleteItems(DynamoHttpServletRequest pRequest,
            DynamoHttpServletResponse pResponse) throws IOException,
            ServletException {
        // just move the the correct page:
        return checkFormRedirect(get("deleteCancelURL").toString(), get(
                "errorUrl").toString(), pRequest, pResponse);
    }

    /**
     * This method handles the deletion of items. Acutally, it just sets
     * 'deleted=true' in a repository item It DOES NOT DELETE the repository
     * item.
     * 
     * @param pRequest
     *            Description of Parameter
     * @param pResponse
     *            Description of Parameter
     * @return boolean True if form processing should continue, false if it
     *         should be aborted.
     * @throws ServletException,
     *             IOException
     * @exception ServletException
     *                Description of Exception
     * @exception IOException
     *                Description of Exception
     */
    public boolean handleDeleteItems(DynamoHttpServletRequest pRequest,
            DynamoHttpServletResponse pResponse) throws IOException,
            ServletException {

        try {
            RepositoryItem[] selectedResults = getSelectedResults();
            if (selectedResults == null || selectedResults.length == 0) {
                logError("No items have been selected to delete.");
            } else {
                MutableRepository repository = (MutableRepository) getRepository();
                for (int i = 0; i < selectedResults.length; i++) {
                    /*
                     * Instead of:
                     * repository.removeItem(selectedResults[i].getRepositoryId(),
                     * "ExternalLink"); which would delete the RepositoryItem
                     * itself, leaving no record.
                     */
                    MutableRepositoryItem itemToUpdate = repository
                            .getItemForUpdate(selectedResults[i]
                                    .getRepositoryId(), "ExternalLink");
                    itemToUpdate.setPropertyValue("StateId", "DELETED");
                    repository.updateItem(itemToUpdate);
                    if (isLoggingDebug()) {
                        logDebug("Deleted permanently: " + "ExternalLink" + ":"
                                + selectedResults[i].getRepositoryId());
                    }
                }
                this.reset();
            }
        } catch (RepositoryException re) {
            logError("An error occurred while accessing the repository: "
                    + re.getMessage());
        }
        // Return to the search page like the other dashboard deletes:
        return handleSearch(pRequest, pResponse);
    }

    private String equalsQuery(String queryParam, String dataParam) {
        return queryParam + " = ?" + getNextParamNum(get(dataParam));
    }

    private String notEqualsQuery(String queryParam, String dataParam) {
        return queryParam + " != ?" + getNextParamNum(get(dataParam));
    }

    // ///////////////////////////////

    /*
     * Multiple Item type search aggregation
     * 
     * public void doSearch() { try {
     * 
     * if (isLoggingDebug()){ logDebug("BEGIN: doSearch(), getValue() = " +
     * getValue()); }
     * 
     * resetFormExceptions(); resetParameters();
     * 
     * String itemTypes = getItemTypes(); String[] itemTypeArray =
     * itemTypes.split(",");
     * 
     * for (int searchIndex=0; searchIndex<itemTypeArray.length; searchIndex++) {
     * 
     * String itemType = itemTypeArray[searchIndex];
     * 
     * if (isLoggingDebug()){ logDebug("BEGIN: Build query for " + itemType); }
     * 
     * String query = "";
     *  // shared content? boolean isSharedQuery = new
     * Boolean((String)get("querySharedOnly")).booleanValue();
     * 
     * if (isSharedQuery) { query += notEqualsQuery("OrigSiteId", "siteId"); }
     * else { query += equalsQuery("OrigSiteId", "siteId"); }
     * 
     * query = addStateQuery(query);
     * 
     * query = addToQuery(query, "Title CONTAINS IGNORECASE ", "title");
     * 
     * query = addSplitQuery(query, "Keywords CONTAINS IGNORECASE ",
     * "keywords");
     * 
     * query = addDateToQuery(query, "CreateDate > ", "createDateMin"); query =
     * addDateToQuery(query, "CreateDate < ", "createDateMax");
     * 
     * query = addDateToQuery(query, "StartDate > ", "startDateMin"); query =
     * addDateToQuery(query, "StartDate < ", "startDateMax");
     * 
     * query = addIdExclusionQuery(query, itemType);
     * 
     * if (isLoggingDebug()){ logDebug("query = " + query.toString());
     * logDebug("params = " + listParams()); }
     *  // query is built, run it: QueryManager qm = new QueryManager();
     * qm.setRepository(this.getRepository());
     * 
     * qm.setItem(itemType);
     * 
     * qm.setParams(getParams()); qm.setQuery(query.toString());
     * RepositoryItem[] rItems = new RepositoryItem[0]; try { rItems =
     * qm.doQuery(); } catch (RuntimeException e) { logError("handleSearch
     * ERROR: " + e.getMessage()); } int numItems = 0; if (rItems != null) {
     * numItems = rItems.length; } else { rItems = new RepositoryItem[0]; } if
     * (isLoggingDebug()){ logDebug(itemType + " query found " + numItems + " " +
     * itemType + "s, rItems = " + rItems); } set("searchResults" + searchIndex,
     * rItems); } set("numOfSearches", new Integer(itemTypeArray.length));
     *  } catch (Exception e) { String message = "exception = " + e.toString() + ",
     * message = " + e.getMessage(); logError(message); } }
     */

    public void doSearch() {
        try {

            if (isLoggingDebug()) {
                logDebug("BEGIN: doSearch(), getValue() = " + getValue());
            }

            resetFormExceptions();
            resetParameters();

            Object now = new Date();

            String itemType = (String) get("selectedItemType");

            if (isLoggingDebug()) {
                logDebug("BEGIN: Build query for " + itemType);
            }

            String query = "";

            // shared content?
            boolean isSharedQuery = new Boolean((String) get("querySharedOnly"))
                    .booleanValue();

            if (isSharedQuery) {
                query += notEqualsQuery("OrigSiteId", "siteId");
            } else {
                query += equalsQuery("OrigSiteId", "siteId");
            }

            boolean includeDeleted = new Boolean((String) get("includeDeleted"))
                    .booleanValue();
            Map states = new HashMap();
            String state = (String) get("state");
            if (!"".equals(state)) {
                // only include the selected state
                states.put(state, state);
                if (includeDeleted) {
                    // include the 2 deleted states as well
                    states.put("DELETED", "DELETED");
                    states.put("PRODDELTD", "PRODDELTD");
                }
            } else {
                if (includeDeleted) {
                    // basically, we want EVERY STATE, so don't build this into
                    // the query
                } else {
                    // everything BUT the deleted states.
                    states.put("APPROVED", "APPROVED");
                    states.put("NEWAUTHORED", "NEWAUTHORED");
                    states.put("PRODDEPLOY", "PRODDEPLOY");
                    states.put("EXPIRED", "EXPIRED");
                    states.put("CPSHOLD", "CPSHOLD");
                    states.put("EXCEPTION", "EXCEPTION");
                    states.put("REJECTED", "REJECTED");
                    states.put("NEWUPLOADED", "NEWUPLOADED");
                }
            }

            int statesSize = states.size();
            if (statesSize > 0) {
                // adding state to query:
                boolean firstState = true;
                Set statesToAdd = states.keySet();
                Iterator stateIterator = statesToAdd.iterator();
                query += " AND (";
                while (stateIterator.hasNext()) {
                    String nextState = (String) stateIterator.next();
                    if (firstState) {
                        query += "StateId = \"" + nextState + "\"";
                        firstState = false;
                    } else {
                        query += " OR StateId = \"" + nextState + "\"";
                    }
                }
                query += ")";
            }

            // handle inclusion or exclusion of date/time expired items:
            boolean includeExpired = new Boolean((String) get("includeExpired"))
                    .booleanValue();
            if (includeExpired) {
                // query += " AND EndDate <= ?" + getNextParamNum(now);
            } else {
                query += " AND (EndDate > ?" + getNextParamNum(now)
                        + " OR EndDate IS NULL)";
            }

            query = addToQuery(query, "Title CONTAINS IGNORECASE ", "title");

            query = addSplitQuery(query, "Keywords CONTAINS IGNORECASE ",
                    "keywords");

            query = addDateToQuery(query, "CreateDate > ", "createDateMin");
            query = addDateToQuery(query, "CreateDate < ", "createDateMax");

            query = addDateToQuery(query, "StartDate > ", "startDateMin");
            query = addDateToQuery(query, "StartDate < ", "startDateMax");

            query = addIdExclusionQuery(query, itemType);

            if (isLoggingDebug()) {
                logDebug("query = " + query.toString());
                logDebug("params = " + listParams());
            }

            // query is built, run it:
            QueryManager qm = new QueryManager();
            qm.setLoggingDebug(true);
            
            qm.setRepository(this.getRepository());

            qm.setItem(itemType);

            qm.setParams(getParams());
            qm.setQuery(query.toString());

            RepositoryItem[] rItems = new RepositoryItem[0];
            try {
                rItems = qm.doQuery();
            } catch (RuntimeException e) {
                logError("handleSearch ERROR: " + e.getMessage());
            }

            int numItems = 0;
            if (rItems != null) {
                numItems = rItems.length;
            } else {
                rItems = new RepositoryItem[0];
            }

            if (isLoggingDebug()) {
                logDebug("UNFILTERED " + itemType + " query found " + numItems + " " + itemType
                        + "s, rItems = " + rItems);
            }

            // groups filter needs to be applied to results
            // example: selectedContentGroups=28|Business~81|Classifieds
            String selectedContentGroups = (String) get("selectedContentGroups");
            RepositoryItem[] filteredItems = filterByGroups(rItems, selectedContentGroups);

            if (isLoggingDebug()) {
                logDebug("selectedContentGroups = " + selectedContentGroups);
            }

            int numFilteredItems = 0;
            if (filteredItems != null) {
                numFilteredItems = filteredItems.length;
            } else {
                filteredItems = new RepositoryItem[0];
            }

            if (isLoggingDebug()) {
                logDebug("GROUP FILTERED " + itemType + " query found " + numFilteredItems + " " + itemType
                        + "s, rItems = " + filteredItems);
            }

            int maxResults = this.getMaxResultsInt();

            if (numFilteredItems > maxResults) {
                numFilteredItems = 0;
                filteredItems = new RepositoryItem[0];
                set("searchErrorMessage", this.getMaxResultsMsg());
                if (isLoggingDebug()) {
                    logDebug("The maximum ["
                            + maxResults
                            + "] results has been exceeded, display error message.");
                }
            } else if (numFilteredItems == 0) {
                set("searchErrorMessage", this.getMinResultsMsg());
                if (isLoggingDebug()) {
                    logDebug("No results were found, display error message.");
                }
            } else {
                set("searchErrorMessage", "");
            }

            set("searchResults", filteredItems);

        } catch (Exception e) {
            String message = "exception = " + e.toString() + ", message = "
                    + e.getMessage();
            logError(message);
        }
    }
    /*
    public RepositoryItem[] getResults() {
        doSearch();
        String currentSort = getCurrentSort();
        if (isLoggingDebug()) {
            logDebug("BEGIN: getResults()");
        }
        DynamicRepositoryItemComparator dynamicComparator = new DynamicRepositoryItemComparator(
                this);

        // if you want the comparator to send debug to this service, set it
        // true:
        dynamicComparator.setLoggingDebug(false);

        dynamicComparator.setSortType(currentSort);
        dynamicComparator.setSortDirection(getSortDirection());
        if (isLoggingDebug()) {
            logDebug("dynamicComparator = " + dynamicComparator);
        }
        TreeSet sorter = new TreeSet(dynamicComparator);
        */
        /*
         * multiple search aggregation:
         * 
         * int numOfSearches = ((Integer) get("numOfSearches")).intValue(); for
         * (int searchIndex=0; searchIndex<numOfSearches; searchIndex++) {
         * RepositoryItem[] queryResults = (RepositoryItem[])
         * get("searchResults" + searchIndex); try {
         * sorter.addAll(Arrays.asList(queryResults)); } catch (Throwable t) {
         * logError("Throwable caught in getResults() during sort, error was: " +
         * t.getMessage()); }
         *  }
         */
        /*
        // RepositoryItem[] queryResults = (RepositoryItem[])
        // get("searchResults");
        // RepositoryItem[] queryResults = getFilteredResults();
        RepositoryItem[] queryResults = (RepositoryItem[]) get("searchResults");

        try {
            sorter.addAll(Arrays.asList(queryResults));
        } catch (Throwable t) {
            logError("Throwable caught in getResults() during sort, error was: "
                    + t.getMessage());
        }

        ArrayList aList = new ArrayList(sorter);
        int theResultsSize = aList.size();
        int theMaxWeAllow = this.getMaxResultsInt();
        if (theResultsSize <= theMaxWeAllow) {
            set("lessThanMax", "true");
        } else {
            set("lessThanMax", "false");
        }

        RepositoryItem[] results = new RepositoryItem[theResultsSize];
        if (isLoggingDebug()) {
            logDebug("END: getResults()");
        }
        return (RepositoryItem[]) aList.toArray(results);
    }
    */
    
    public DynamicBean[] getResults() {
      doSearch();
      String currentSort = getCurrentSort();
      if (isLoggingDebug()) {
          logDebug("BEGIN: getResults()");
      }
      DynamicRepositoryItemComparator dynamicComparator = new DynamicRepositoryItemComparator(
              this);

      // if you want the comparator to send debug to this service, set it
      // true:
      dynamicComparator.setLoggingDebug(false);

      dynamicComparator.setSortType(currentSort);
      dynamicComparator.setSortDirection(getSortDirection());
      if (isLoggingDebug()) {
          logDebug("dynamicComparator = " + dynamicComparator);
      }
      TreeSet sorter = new TreeSet(dynamicComparator);
      
      /*
       * multiple search aggregation:
       * 
       * int numOfSearches = ((Integer) get("numOfSearches")).intValue(); for
       * (int searchIndex=0; searchIndex<numOfSearches; searchIndex++) {
       * RepositoryItem[] queryResults = (RepositoryItem[])
       * get("searchResults" + searchIndex); try {
       * sorter.addAll(Arrays.asList(queryResults)); } catch (Throwable t) {
       * logError("Throwable caught in getResults() during sort, error was: " +
       * t.getMessage()); }
       *  }
       */
      
      // RepositoryItem[] queryResults = (RepositoryItem[])
      // get("searchResults");
      // RepositoryItem[] queryResults = getFilteredResults();
      RepositoryItem[] queryResults = (RepositoryItem[]) get("searchResults");

      try {
          sorter.addAll(Arrays.asList(queryResults));
      } catch (Throwable t) {
          logError("Throwable caught in getResults() during sort, error was: "
                  + t.getMessage());
      }

      ArrayList aList = new ArrayList(sorter);
      int theResultsSize = aList.size();
      int theMaxWeAllow = this.getMaxResultsInt();
      if (theResultsSize <= theMaxWeAllow) {
          set("lessThanMax", "true");
      } else {
          set("lessThanMax", "false");
      }

      //RepositoryItem[] results = new RepositoryItem[theResultsSize];
      if (isLoggingDebug()) {
          logDebug("END: getResults()");
      }
      //return (RepositoryItem[]) aList.toArray(results);
      return convertToDynamicBeanArray(aList);
    }
    
    public DynamicBean[] convertToDynamicBeanArray(List results) {
      DynamicBean[] dBeans = null;
      if (results != null) {
        int size = results.size();
        dBeans = new DynamicBean[size];
        ListIterator lit = results.listIterator();
        int index = 0;
        while (lit.hasNext()) {
          RepositoryItem rItem = (RepositoryItem) lit.next();
          DynamicBean dBean = new DynamicBean();
          
          // repositoryId, title
          dBean.set("repositoryId", rItem.getRepositoryId());
          dBean.set("title", (String) rItem.getPropertyValue("title"));
          
          // origSite
          DynamicBean siteBean = new DynamicBean();
          RepositoryItem site = (RepositoryItem) rItem.getPropertyValue("origSite");
          String siteName = (String) site.getPropertyValue("name");
          siteBean.set("name", siteName);
          dBean.set("origSite", siteBean);
          
          // state
          DynamicBean stateBean = new DynamicBean();
          RepositoryItem state = (RepositoryItem) rItem.getPropertyValue("state");
          String stateName = (String) state.getPropertyValue("name");
          stateBean.set("name", stateName);
          dBean.set("state", stateBean);
          
          // createDate, startDate
          dBean.set("createDate", (Date) rItem.getPropertyValue("createDate"));
          dBean.set("startDate", (Date) rItem.getPropertyValue("startDate"));
          
          // special value types:
          dBean.set("atgId", rItem.toString());
          
          // extras per type:
          String contentType = (String) rItem.getPropertyValue("ContentType");
          // only one extra so far, url if a media type:
          if ("image".equalsIgnoreCase(contentType)
              || "binary".equalsIgnoreCase(contentType)
              || "flash".equalsIgnoreCase(contentType)) {
            
              // url
              dBean.set("url", (String) rItem.getPropertyValue("url"));
          }
          
          //this.logDebug(index + " = " + dBean.xmlize());
          dBeans[index++] = dBean;
        }
      }
      return dBeans;
    }
    
    public String getCurrentSort() {
        String result = "";
        String sortBeforeThis = getLastSort();
        Object sortBy = get("sortBy");
        if (sortBy != null && !"".equals(sortBy)) {
            String thisSort = sortBy.toString();
            if (sortBeforeThis != null && sortBeforeThis.equals(thisSort)) {
                // they are the same, flip them:
                setSortDirection((-1) * getSortDirection());
                result = sortBeforeThis;
            } else {
                // they are different:
                setLastSort(thisSort);
                result = thisSort;
            }
        } else {
            // sortBy is null or "".
            if (sortBeforeThis == null || "".equals(sortBeforeThis)) {
                // load the default:
                // Changed order as part of CR 2675
                setSortDirection(-1);
                String defaultSortby = getDefaultSortBy();
                setLastSort(defaultSortby);
                result = defaultSortby;
            } else {
                // had something last time, do nothing...
                result = sortBeforeThis;
            }
        }
        // in any case, clear out "sortBy" so we can tell when the search button
        // is clicked:
        set("sortBy", "");
        return result;
    }

    public void reset() {
        // reset form:
        set("previouslySubmitted", "false");

        /*
         * was set-up for multiple item types/searches: Integer number =
         * (Integer) get("numOfSearches"); if (number != null) { int
         * numOfSearches = number.intValue(); for (int searchIndex=0;
         * searchIndex<numOfSearches; searchIndex++) { set("searchResults" +
         * searchIndex, new RepositoryItem[0]); } set("numOfSearches", new
         * Integer(0)); }
         */

        set("searchResults", new RepositoryItem[0]);
        set("selectedItemType", "");
        set("lessThanMax", "true");
        // reset defaults:
        initializeDefaultValues(getValue());
        numParams = 0;
        params = null;
        set("selectedResults", new RepositoryItem[0]);
    }

    public boolean getMultipleItemTypes() {
        boolean result = false;
        String types = getItemTypes();
        if (types != null) {
            result = (!(types.indexOf(",") == -1));
        }
        return result;
    }

    public String[] getEachItemType() {
        String[] itemTypeArray = {};
        String types = getItemTypes();
        if (types != null) {
            itemTypeArray = types.split(",");
            // try to initialize "selected" type:
            if (itemTypeArray != null && itemTypeArray.length > 0) {
                String currentSelectedType = (String) get("selectedItemType");
                if (currentSelectedType == null
                        || "".equals(currentSelectedType)) {
                    set("selectedItemType", itemTypeArray[0]);
                } else {
                    boolean match = false;
                    for (int x = 0; x < itemTypeArray.length; x++) {
                        if (currentSelectedType.equals(itemTypeArray[x])) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) {
                        set("selectedItemType", itemTypeArray[0]);
                    }
                }
            }
        }
        return itemTypeArray;
    }

    private static final int MAX_PARAMS = 1000;

    private int numParams = 0;
    
    private String selectedSites = null;
    
    public String getSelectedSites() {
        String result = selectedSites;
    /*
        String temp = (String) getContextSitesMap().get(getContext());
        if (temp != null) {
            result = temp;
        } else {
            RepositoryItem tempSite = getSite();
            String siteName = (String) tempSite.getPropertyValue("Name");
            String siteId = (String) tempSite.getPropertyValue("Id");
            result = siteId+"|"+siteName;
        }
*/
        return result;
    }

    public void setSelectedSites(String selectedSitesVal) {
    	this.selectedSites = selectedSitesVal;
    }

    private Object[] params = null;

    private Repository repository = null;

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    private String[] defaultValues = new String[0];

    public String[] getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(String[] defaultValues) {
        this.defaultValues = defaultValues;
    }

    private String itemTypes = null;

    public String getItemTypes() {
        return itemTypes;
    }

    public void setItemTypes(String type) {
        this.itemTypes = type;
    }

    private String lastSort = "";

    public String getLastSort() {
        return lastSort;
    }

    public void setLastSort(String lastSort) {
        this.lastSort = lastSort;
    }

    private int sortDirection = 1;

    public int getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(int sortDirection) {
        this.sortDirection = sortDirection;
    }

    private String defaultSortBy = "";

    public String getDefaultSortBy() {
        return defaultSortBy;
    }

    public void setDefaultSortBy(String defaultSortBy) {
        this.defaultSortBy = defaultSortBy;
    }

    private String excludeIds = null;

    public String getExcludeIds() {
        return this.excludeIds;
    }

    public void setExcludeIds(String arg) {
        this.excludeIds = arg;
    }

    public String[] getExcludeIdsArray() {
        return this.excludeIds.split(",");
    }

    private Integer maxAllowed = null;

    public Integer getMaxAllowed() {
        return this.maxAllowed;
    }

    public void setMaxAllowed(Integer arg) {
        this.maxAllowed = arg;
    }

    public int getMaxAllowedInt() {
        if (maxAllowed == null) {
            setMaxAllowed(new Integer(0));
        }
        return maxAllowed.intValue();
    }

    private String titleCropped = "";

    public String getTitleCropped() {
        return titleCropped;
    }

    public void setTitleCropped(String arg) {
        titleCropped = arg;
    }

    private Integer maxResults = null;

    public Integer getMaxResults() {
        return this.maxResults;
    }

    public void setMaxResults(Integer arg) {
        this.maxResults = arg;
    }

    public int getMaxResultsInt() {
        if (maxResults == null) {
            setMaxResults(new Integer(0));
        }
        return maxResults.intValue();
    }

    private String maxResultsMsg = null;

    public String getMaxResultsMsg() {
        return maxResultsMsg;
    }

    public void setMaxResultsMsg(String arg) {
        this.maxResultsMsg = arg;
    }

    private String minResultsMsg = null;

    public String getMinResultsMsg() {
        return minResultsMsg;
    }

    public void setMinResultsMsg(String arg) {
        this.minResultsMsg = arg;
    }

    private RepositoryItem[] selectedResults = null;

    public RepositoryItem[] getSelectedResults() {
        if (selectedResults == null) {
            setSelectedResults(new RepositoryItem[0]);
        }
        return selectedResults;
    }

    public void setSelectedResults(RepositoryItem[] results) {
        selectedResults = results;
    }

    private String context = null;

    public String getContext() {
        if (context == null) {
            context = "[no context]";
        }
        return context;
    }

    public void setContext(String context) {
        if (context == null) {
            context = "";
        }
        if (!context.equalsIgnoreCase(this.context)) {
            if (isLoggingDebug()) {
                logDebug(" ++++++++++++++++++ CHANGING CONTEXT -- reset()");
            }
            reset();
        }

        this.context = context;
    }

    // The root of the context map.
    private Map rootMap = null;

    public Map getRootMap() {
        if (rootMap == null) {
            rootMap = new HashMap();
        }
        return rootMap;
    }

    public void setRootMap(Map arg) {
        this.rootMap = arg;
    }

    // The session-based, context-based map of maps.
    public Map getValue() {
        // get local copy
        String context = getContext();
        Map rootContext = getRootMap();
        Map contextMap = (Map) rootContext.get(context);
        if (contextMap == null) {
            contextMap = new LoggingHashMap(this);
            contextMap.put("JSP_CONTEXT", context);
            initializeDefaultValues(contextMap);
            rootContext.put(context, contextMap);
        }
        if (isLoggingDebug()) {
            // logDebug("getValue() contextMap = " + contextMap);
        }

        return contextMap;
    }

    public void setValue(Map contextMap) {
        getRootMap().put(getContext(), contextMap);
        if (isLoggingDebug()) {
            logDebug("setValue getRootMap().put(" + getContext() + ", "
                    + contextMap + ")");
        }

    }

    public Object get(String key) {
        return getValue().get(key);
    }

    public void set(Object key, Object value) {
        getValue().put(key, value);
    }

    // A work-around for an ATG limitation whereby previous values
    // won't be reset to empty, when the new value is null or blank:
    // this needs to be called 1st, i.e. have a HIGH priority in
    // your DSP Input tag.
    private String[] doNotClearOnForceDataReset = null;
    public void setDoNotClearOnForceDataReset(String[] aSet) {
        doNotClearOnForceDataReset = aSet;
        if(isLoggingDebug()){
          logDebug("doNotClearOnForceDataReset = " + Arrays.asList(doNotClearOnForceDataReset));
        }
    }
    public String[] getDoNotClearOnForceDataReset() {
        return doNotClearOnForceDataReset;
    }
    public List getRetainList() {
        return Arrays.asList(getDoNotClearOnForceDataReset());
    }
    public String getForceDataReset() {
        return "do_not_force";
    }

    public void setForceDataReset(String value) {
        if ("FORCE_RESET".equals(value)) {
            try {
                getValue().keySet().retainAll(getRetainList());
            } catch (RuntimeException e) {
                logError("setForceDataReset error, getRetainList() = " + getRetainList() + ", getValue() = " + getValue(), e);
            }
        }
    }
    
    
    private MngiProfile profile = null;

    public MngiProfile getProfile() {
        return this.profile;
    }

    public void setProfile(MngiProfile arg) {
        this.profile = arg;
    }

    public String getTimeZoneShort() {
        String timeZoneName = getProfile().getCurrentSite().getPropertyValue(
                "timezone").toString();
        return DateTimeHelper.getTimeZoneShort(new Date(), timeZoneName);
    }

    public class LoggingHashMap extends HashMap {

        private static final long serialVersionUID = 1890679629932104384L;

        private GenericFormHandler service = null;

        public LoggingHashMap(GenericFormHandler service) {
            this.service = service;
        }

        private void log(String msg) {
            if (service != null) {
                // service.logDebug("LoggingHashMap: " + msg);
            }
        }

        public Object put(Object key, Object value) {
            Object previous = super.put(key, value);
            log("put(" + key + "," + value + ") = " + previous);
            return previous;
        }

        public Object get(Object key) {
            Object previous = super.get(key);
            log("get(" + key + ") = " + previous);
            return previous;
        }
    }
}
