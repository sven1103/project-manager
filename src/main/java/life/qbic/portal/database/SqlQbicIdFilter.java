package life.qbic.portal.database;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sven1103 on 21/12/16.
 */
public class SqlQbicIdFilter implements Container.Filter {

    protected String propertyId;

    protected List<String> stringsToFilter;

    public SqlQbicIdFilter(String propertyID, List<String> stringsToFilter){
        this.propertyId = propertyID;
        updateFilterList(stringsToFilter);
    }


    @Override
    public boolean passesFilter(Object o, Item item) throws UnsupportedOperationException {
        Property p = item.getItemProperty(propertyId);

        if (p == null || !p.getType().equals(String.class))
            return false;

        String value = (String) p.getValue();

        return stringsToFilter.contains(value);
    }

    @Override
    public boolean appliesToProperty(Object o) {
        return propertyId != null &&
                propertyId.equals(this.propertyId);
    }

    public void updateFilterList(List<String> filterStrings){
        if (filterStrings == null){
            this.stringsToFilter = new ArrayList<>();
        } else{
            this.stringsToFilter = filterStrings;
        }

    }
}
