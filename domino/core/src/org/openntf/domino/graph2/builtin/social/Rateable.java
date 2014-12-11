package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;

@JavaHandlerClass(Rateable.RateableImpl.class)
public interface Rateable extends DVertexFrame {
	@JavaHandler
	public double getRaterRating(Rater rater);

	@JavaHandler
	public double getAverageRating();

	@IncidenceUnique(label = Rates.LABEL_RATES, direction = Direction.IN)
	public Iterable<Rates> getRates();

	@IncidenceUnique(label = Rates.LABEL_RATES, direction = Direction.IN)
	public Rates addRates(Rater rater);

	@IncidenceUnique(label = Rates.LABEL_RATES, direction = Direction.IN)
	public Rates findRates(Rater rater);

	@IncidenceUnique(label = Rates.LABEL_RATES, direction = Direction.IN)
	public void removeRates(Rater rater);

	@AdjacencyUnique(label = Rates.LABEL_RATES, direction = Direction.IN)
	public Iterable<Rater> getRatesRater();

	@AdjacencyUnique(label = Rates.LABEL_RATES, direction = Direction.IN)
	public Rater addRatesRater(Rater rater);

	public abstract static class RateableImpl implements Rateable {
		private transient double avgRating_ = Double.NaN;

		@Override
		public double getAverageRating() {
			if (avgRating_ == Double.NaN) {
				Iterable<Rates> rates = getRates();
				if (rates != null) {
					long count = 0l;
					long total = 0l;
					for (Rates rate : rates) {
						count++;
						total += rate.getRating();
					}
					avgRating_ = total / count;
				}
			}
			return avgRating_;
		}

		@Override
		public double getRaterRating(final Rater rater) {
			Rates raterRates = findRates(rater);
			if (raterRates != null) {
				return raterRates.getRating();
			}
			return getAverageRating();
		}
	}
}