
package pt.ornrocha.arrays;


// method from http://stackoverflow.com/questions/951848/java-array-sort-quick-way-to-get-a-sorted-list-of-indices-of-an-array @autor:bobfoster
public class QuickSortArray {

	public interface Array {
	    int cmp(int aindex, int bindex);
	    void swap(int aindex, int bindex);
	    int length();
	}

	public static void quicksort(Array a) {
	    quicksort(a, 0, a.length() - 1);
	}

	public static void quicksort(Array a, int left, int right) {
	    if (right <= left) return;
	    int i = partition(a, left, right);
	    quicksort(a, left, i-1);
	    quicksort(a, i+1, right);
	}

	public static boolean isSorted(Array a) {
	    for (int i = 1, n = a.length(); i < n; i++) {
	        if (a.cmp(i-1, i) > 0)
	            return false;
	    }
	    return true;
	}

	private static int mid(Array a, int left, int right) {
	    // "sort" three elements and take the middle one
	    int i = left;
	    int j = (left + right) / 2;
	    int k = right;
	    // order the first two
	    int cmp = a.cmp(i, j);
	    if (cmp > 0) {
	        int tmp = j;
	        j = i;
	        i = tmp;
	    }
	    // bubble the third down
	    cmp = a.cmp(j, k);
	    if (cmp > 0) {
	        cmp = a.cmp(i, k);
	        if (cmp > 0)
	            return i;
	        return k;
	    }
	    return j;
	}

	private static int partition(Array a, int left, int right) {
	    int mid = mid(a, left, right);
	    a.swap(right, mid);
	    int i = left - 1;
	    int j = right;

	    while (true) {
	        while (a.cmp(++i, right) < 0)
	            ;
	        while (a.cmp(right, --j) < 0)
	            if (j == left) break;
	        if (i >= j) break;
	        a.swap(i, j);
	    }
	    a.swap(i, right);
	    return i;
	}

	public static class IndexArray implements Array {
	    int[] index;
	    Array a;

	    public IndexArray(Array a) {
	        this.a = a;
	        index = new int[a.length()];
	        for (int i = 0; i < a.length(); i++)
	            index[i] = i;
	    }

	    /**
	     * Return the index after the IndexArray is sorted.
	     * The nested Array is unsorted. Assume the name of
	     * its underlying array is a. The returned index array
	     * is such that a[index[i-1]] <= a[index[i]] for all i
	     * in 1..a.length-1.
	     */
	    public int[] index() {
	        int i = 0;
	        int j = index.length - 1;
	        while (i < j) {
	            int tmp = index[i];
	            index[i++] = index[j];
	            index[j--] = tmp;
	        }
	        int[] tmp = index;
	        index = null;
	        return tmp;
	    }

	    @Override
	    public int cmp(int aindex, int bindex) {
	        return a.cmp(index[aindex], index[bindex]);
	    }

	    @Override
	    public void swap(int aindex, int bindex) {
	        int tmp = index[aindex];
	        index[aindex] = index[bindex];
	        index[bindex] = tmp;
	    }

	    @Override
	    public int length() {
	        return a.length();
	    }

	}
}