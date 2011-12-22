package eu.alertproject.iccs.stardom.lsa.tfidf;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.SingularValueDecomposition;
import org.apache.commons.math.linear.SingularValueDecompositionImpl;

/**
 * Uses Latent Semantic Indexing to find word associations between docs. 
 * Idea is to find the intersections of the words found in each document
 * and score them accordingly. We use SVD to accomplish this. We first
 * decompose the word frequency vector into the three parts, then multiply
 * the three components back to get our transformed matrix.
 * @author Sujit Pal
 * @version $Revision: 44 $
 */
public class LsiIndexer implements Transformer<RealMatrix,RealMatrix> {

  public RealMatrix transform(RealMatrix matrix) {
    // phase 1: Singular value decomposition
    SingularValueDecomposition svd = new SingularValueDecompositionImpl(matrix);
    RealMatrix wordVector = svd.getU();
    RealMatrix sigma = svd.getS();
    RealMatrix documentVector = svd.getV();
    // compute the value of k (ie where to truncate)
    int k = (int) Math.floor(Math.sqrt(matrix.getColumnDimension()));
    RealMatrix reducedWordVector = wordVector.getSubMatrix(
      0, wordVector.getRowDimension() - 1, 0, k - 1);
    RealMatrix reducedSigma = sigma.getSubMatrix(0, k - 1, 0, k - 1);
    RealMatrix reducedDocumentVector = documentVector.getSubMatrix(
      0, documentVector.getRowDimension() - 1, 0, k - 1);
    RealMatrix weights = reducedWordVector.multiply(
      reducedSigma).multiply(reducedDocumentVector.transpose());
    // Phase 2: normalize the word scrores for a single document
    for (int j = 0; j < weights.getColumnDimension(); j++) {
      double sum = sum(weights.getSubMatrix(0, weights.getRowDimension() - 1, j, j));
      for (int i = 0; i < weights.getRowDimension(); i++) {
        if (sum > 0.0D) {
          weights.setEntry(i, j, Math.abs((weights.getEntry(i, j)) / sum));
        } else {
          weights.setEntry(i, j, 0.0D);
        }
      }
    }
    return weights;
  }

  private double sum(RealMatrix colMatrix) {
    double sum = 0.0D;
    for (int i = 0; i < colMatrix.getRowDimension(); i++) {
      sum += colMatrix.getEntry(i, 0);
    }
    return sum;
  }
}
