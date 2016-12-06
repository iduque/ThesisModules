/*
 * PacientesTableModel.java
 */

package View;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
/**
 * Table model asociado a la <tt>JTable</tt> que muestra los resultados de la
 * busqueda de pacientes.
 * @author RPulido
 * @version 1.0
 */
public class TableModel extends AbstractTableModel {
    
    // - Atributos ---------------------------------------------------------
    
    /**
     * Nombre de las columnas de la tabla.
     */
    private String[] _columnNames = {"User", "Questionnaire Name", "Select"};
    
    /**
     * Datos de la tabla.
     */
    private List<List> _data = new ArrayList();
    
    // - Métodos -----------------------------------------------------------
    
    /** 
     * Constructor.
     * <p>
     * Crea una nueva instancia de <tt>PacientesTableModel</tt>.
     */
    public TableModel() {
        super();
    }

    
    /**
     * @return El número de filas que contiene actualmente la tabla.
     */
    public int getRowCount() {
        return _data.size();
    }

    /**
     * @return El número de columnas que contiene actualmente la tabla.
     */
    public int getColumnCount() {
        return _columnNames.length;
    }

    /**
     * @param col entero mayor o igual a 0 y menor estricto que el número de
     * columnas de la tabla que indica la columna cuyo nombre se quiere
     * consultar.
     * @return La etiqueta con el nombre de la columna cuyo indice se pasa
     * como argumento.
     */
    @Override
    public String getColumnName(int col) {

        if ((col>=0) && (col<_columnNames.length)) {
            return _columnNames[col];
        } else {
            return "";
        }        
    }

    /**
     * @param row Fila del objeto que se quiere consultar.
     * @param col Columna del objeto que se quiere consultar.
     * @return El objeto que ocupa la posici�n cuya fila es <tt>row</tt> y
     * cuya columna es <tt>col</tt>.
     */
    public Object getValueAt(int row, int col) {
        if (((row>=0) && (row<_data.size())) &&
                ((col>=0) &&(col<_columnNames.length))) {
            return _data.get(row).get(col);
        } else {
            return null;
        }
    }
    
    /**
     * Este método es usado por Swing para saber cómo debe representar los
     * datos de la tabla. Por ejemplo, si el tipo es <tt>Boolean</tt> por
     * defecto Swing lo representa como un check box.
     * @param col Entero mayor o igual que cero y menor estricto que el número
     * de columnas de la tabla que indica la columna que ocupan los datos
     * cuyo tipo se quiere consultar.
     * @return La clase (el tipo) de los datos que se muestran en la columna
     * cuyo índice se pasa como argumento.
     */
    @Override
    public Class getColumnClass(int col) {
        if(col == 3) return Boolean.class;
        else return getValueAt(0, col).getClass();
    }
    
    /**
     * @param row Fila del dato que se quiere consultar.
     * @param col Columna del dato que se quiere consultar.
     * @return <tt>true</tt> si el dato que ocupa la posición cuya fila y
     * columna se pasan como argumentos es editable y <tt>false</tt> en caso
     * contrario.
     */
    public boolean isCellEditable(int row, int col) {
        
         //- Ejemplo concreto para hacer la primera columna editable.
        if (col==2) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Cambia el contenido de la celda cuya fila y columna se pasan como
     * argumento.
     * @param value Nuevo contenido de la celda.
     * @param row Fila de la celda cuyo contenido se quiere cambiar.
     * @param col Columna de la celda cuyo contenido se quiere cambiar.
     */
    public void setValueAt(Object value, int row, int col) {
        _data.get(row).set(col, value) ;
        fireTableCellUpdated(row, col);
    }
    
    
    /**
     * Add a new row to the table
     * @param rowData List of data (row)
     */
    public void addRow(List rowData){
        this._data.add(rowData);
        fireTableRowsInserted(_data.size() - 1, _data.size() - 1);
    }
    
    /**
     * Tell us if a user is already defined in the table
     * @param user User name that we look for
     * @return Boolean
     */
    public Boolean isUser(String user){
        for(List l: _data){
            if(((String) l.get(0)).equals(user))
                return true;
        }
        return false;
    }
}
