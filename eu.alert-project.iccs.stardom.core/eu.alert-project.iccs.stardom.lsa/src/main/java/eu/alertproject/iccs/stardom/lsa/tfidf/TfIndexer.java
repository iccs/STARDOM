package eu.alertproject.iccs.stardom.lsa.tfidf;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.math.linear.RealMatrix;

/**
 * Normalizes the occurence matrix per document. Divides each entry by the
 * sum of occurence values for that column. That way a longer document which
 * has 2 occurences of a certain word will not be ranked higher than a shorter
 * document with 1 occurence of the word for that word. At the end of this
 * transformation, the values are the frequency of the word in the document.
 * @author Kostas Christidis
 * @version $Revision $
 */
public class TfIndexer implements Transformer<RealMatrix,RealMatrix> {

  public RealMatrix transform(RealMatrix matrix) {
    for (int j = 0; j < matrix.getColumnDimension(); j++) {
      double sum = sum(matrix.getSubMatrix(0, matrix.getRowDimension() -1, j, j));
      for (int i = 0; i < matrix.getRowDimension(); i++) {
        if (sum > 0.0D) {
          matrix.setEntry(i, j, (matrix.getEntry(i, j) / sum));
        } else {
          matrix.setEntry(i, j, 0.0D);
        }
      }
    }
    return matrix;
  }

  private double sum(RealMatrix colMatrix) {
    double sum = 0.0D;
    for (int i = 0; i < colMatrix.getRowDimension(); i++) {
      sum += colMatrix.getEntry(i, 0);
    }
    return sum;
  }
}
