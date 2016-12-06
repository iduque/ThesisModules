/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.util.*;
import javax.swing.table.*;

public class JTableModel extends AbstractTableModel {
        
        private LinkedList<String> _data = new LinkedList<String>();
        private LinkedList<String> _columns = new LinkedList<String>();

        public JTableModel(LinkedList data,LinkedList columns) {

            _data = data;
            _columns = columns;                       
        }

        public void setRowColour(int row) {
            fireTableRowsUpdated(row, row);
        }

        @Override
        public int getRowCount() {
            if (getColumnCount()==0) return 0;
            else  return _data.size() / getColumnCount();
        }

        @Override
        public int getColumnCount() {
            if(_columns.isEmpty()) return 0;
            else  return _columns.size();
        }
        
        
        @Override
        public String getColumnName(int col) {
            if ((col>=0) && (col<_columns.size())) {
                return _columns.get(col).toString();
            } else {
                return "";
            }        
        }
        
        /**
         * Return value from the specified cell
         * @param rowIndex Row
         * @param columnIndex Column
         * @return String with value from the cell
         */
        @Override
        public String getValueAt(int rowIndex, int columnIndex) {
                return (String) _data.get((rowIndex * getColumnCount())
                                + columnIndex);
        }
        
        /**
         * Set the value of the defined row and column on the table
         * @param value New value
         * @param row Row
         * @param col Column
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
            _data.set((row*getColumnCount()+col), value.toString());
            fireTableCellUpdated(row, col);
        }
        
        /**
         * Return the position of the searched value
         * @param sensor Sensor name
         * @return Position of the found value; -1 if it is not
         */
        public int getPositionSensor(String sensor){
            for(int i=0; i<_data.size(); i++){
                if(_data.get(i).equals(sensor)) return i;
            }
            return -1;
        }
        
//        /**
//         * Look for a MAC between all MACs that belong to this room
//         * @param mac String with the searched MAC
//         * @return True if it is found; False if it is not
//         */
//        public boolean isMac(String mac) { 
//            for(int i=0; i<_mac.size();i++)
//                if(mac.equals(_mac.get(i))) return true;
//            
//            return false;
//            
//       }
//       
//       /**
//         * Return the name of the tab (room) for these data
//         * @return String with the room name
//         */ 
//       public String getTab() { return _tab;}
}
