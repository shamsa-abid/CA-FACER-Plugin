package RelatedMethods.CustomUtilities;

import java.util.Comparator;
import java.util.Map;


class IntegerEntryComparator implements Comparator<Map.Entry> {
	public int compare(Map.Entry e1, Map.Entry e2) {
		int result = 0;
		int value = (Integer)e2.getValue() - (Integer)e1.getValue();
		if(value<0)
			result =  -1;
		if(value==0)
			result =  0;
		if(value>0)
			result =  1;
		return result;
		
	}
}