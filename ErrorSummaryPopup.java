import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class ErrorSummaryDialog extends Dialog {

    private ArrayList<ErrorInfo> errorList;

    protected ErrorSummaryDialog(Shell parentShell, ArrayList<ErrorInfo> errorList) {
        super(parentShell);
        this.errorList = errorList;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(1, false);
        container.setLayout(layout);

        TableViewer tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
        tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tableViewer.getTable().setHeaderVisible(true);

        TableLayout tableLayout = new TableLayout();
        tableViewer.getTable().setLayout(tableLayout);

        tableLayout.addColumnData(new ColumnWeightData(30, 150, true));
        tableLayout.addColumnData(new ColumnWeightData(50, 200, true));
        tableLayout.addColumnData(new ColumnWeightData(20, 100, true));

        tableViewer.setContentProvider(ArrayContentProvider.getInstance());

        tableViewer.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                ErrorInfo errorInfo = (ErrorInfo) element;
                switch (columnIndex) {
                    case 0:
                        return errorInfo.getControlType();
                    case 1:
                        return errorInfo.getErrorMessage();
                    case 2:
                        return errorInfo.getErrorObjects().toString();
                    default:
                        return "";
                }
            }
        });

        tableViewer.setInput(errorList);

        return container;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, SWT.OK, "OK", true);
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (buttonId == SWT.OK) {
            close();
        }
    }
}
