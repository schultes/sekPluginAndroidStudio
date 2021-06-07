package drhd.sequalsk.utils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Simplifies the {@link DocumentListener}.
 */
public interface SimpleDocumentListener extends DocumentListener {

    @Override
    default void insertUpdate(DocumentEvent e) {
        documentUpdated(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        documentUpdated(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        documentUpdated(e);
    }

    void documentUpdated(DocumentEvent event);
}
