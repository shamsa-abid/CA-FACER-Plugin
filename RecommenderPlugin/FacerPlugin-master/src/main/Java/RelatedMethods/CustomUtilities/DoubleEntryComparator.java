package RelatedMethods.CustomUtilities;

import java.util.Comparator;
import java.util.Map;


class DoubleEntryComparator implements Comparator<Map.Entry> {
	public int compare(Map.Entry e1, Map.Entry e2) {
		int result = 0;
		double value = (Double)e2.getValue() - (Double)e1.getValue();
		if(value<0)
			result =  -1;
		if(value==0)
			result =  0;
		if(value>0)
			result =  1;
		return result;
		
	}
}