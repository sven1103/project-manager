package life.qbic.portal;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Extension;
import com.vaadin.server.communication.data.RpcDataProviderExtension;
import com.vaadin.ui.Grid;

import java.util.Collection;

/**
 * Created by sven1103 on 28/11/16.
 */
public class MyGrid extends Grid {

    public final ObjectProperty<Boolean> isChanged = new ObjectProperty<>(true, Boolean.class);

    public MyGrid(){
        setEditorEnabled(true);
        setEditorBuffered(true);
    }

    @Override
    public void saveEditor() throws FieldGroup.CommitException{
        super.saveEditor();
        refreshVisibleRows();
        isChanged.setValue(false ? isChanged.getValue() : true);
    }

    /**
     * We need to refresh the rows manually after saving
     */
    private void refreshVisibleRows() {
        Collection<Extension> extensions = getExtensions();
        extensions.stream().filter(extension -> extension instanceof RpcDataProviderExtension).forEach(extension -> {
            ((RpcDataProviderExtension) extension).refreshCache();
        });
    }

}
