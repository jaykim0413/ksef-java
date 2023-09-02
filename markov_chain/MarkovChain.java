package markov_chain;

import java.util.*;

public class MarkovChain {
    private final int[][] absorbingStateMarkovMatrix;
    private final int[] absorbingState;
    private final int numOfChars;
    private final int lengthOfCase;

    private final Map<Integer, ArrayList<Integer>> states = new HashMap<>();

    public MarkovChain(int[] absorbingState, int numOfChars) {
        this.absorbingState = absorbingState;
        this.lengthOfCase = this.absorbingState.length;
        this.numOfChars = numOfChars;

        this.absorbingStateMarkovMatrix = new int[lengthOfCase][];

        for (int i = 0;i < lengthOfCase;i++) {
            ArrayList<Integer> state = new ArrayList<>();

            for (int j = 0;j < i;j++) {
                state.add(absorbingState[j]);
            }

            states.put(i, state);
        }

        this.generateMarkovChainMatrix();
    }

    private void generateMarkovChainMatrix() {
        for (int i = 0;i < lengthOfCase;i++) {
            this.absorbingStateMarkovMatrix[i] = stateTransitionProbability(listToArray(states.get(i)));
        }
    }

    private int[] stateTransitionProbability(int[] currentState) {
        ArrayList<Integer> curState = arrayToList(currentState);
        int[] probabilities = new int[lengthOfCase + 1];

        for (int i = 0;i < numOfChars;i++) {
            if (curState.size() + 1 != absorbingState.length && i == absorbingState[curState.size()]) {
                probabilities[curState.size() + 1] += 1;
            }
            else {
                curState.add(i);

                probabilities[reduceState(curState).size()] += 1;

                curState.remove(curState.size() - 1);
            }
        }

        return probabilities;
    }

    private ArrayList<Integer> arrayToList(int[] array) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i : array) {
            result.add(i);
        }

        return result;
    }

    private int[] listToArray(ArrayList<Integer> arrayList) {
        int[] result = new int[arrayList.size()];
        int idx = 0;
        for (Integer i : arrayList) {
            result[idx] = i;
            idx++;
        }

        return result;
    }

    private ArrayList<Integer> reduceState(ArrayList<Integer> state) {
        ArrayList<Integer> reduced = deepCopy(state);
        int size;

        do {
            size = reduced.size();
            for (int i = 0;i < reduced.size();i++) {
                if (reduced.get(i) != absorbingState[i]) {
                    reduced.subList(0, i == 0 ? 1: i).clear();
                    break;
                }
            }
        } while (size != reduced.size());

        return reduced;
    }

    public double[][] getMarkovMatrix() {
        double[][] result = new double[lengthOfCase + 1][lengthOfCase + 1];

        for (int i = 0;i < lengthOfCase;i++) {
            for (int j = 0;j < lengthOfCase + 1;j++) {
                result[i][j] = absorbingStateMarkovMatrix[i][j] / (double) numOfChars;
            }
        }

        for (int i = 0;i < lengthOfCase;i++) {
            result[lengthOfCase][i] = 0.0;
        }

        result[lengthOfCase][lengthOfCase] = 1.0;

        return result;
    }

    public String[][] getAbsorbingStateMarkovMatrix() {
        String[][] result = new String[lengthOfCase + 1][lengthOfCase + 1];

        for (int i = 0;i < lengthOfCase;i++) {
            for (int j = 0;j < lengthOfCase + 1;j++) {
                result[i][j] = (absorbingStateMarkovMatrix[i][j] != 0) ? absorbingStateMarkovMatrix[i][j] + " / " + numOfChars : "0.0";
            }
        }
        for (int i = 0;i < lengthOfCase;i++) {
            result[lengthOfCase][i] = "0.0";
        }
        result[lengthOfCase][lengthOfCase] = "1.0";

        return result;
    }

    public int[] getAbsorbingState() {
        return absorbingState;
    }

    public int getNumOfChars() {
        return numOfChars;
    }

    private ArrayList<Integer> deepCopy(ArrayList<Integer> integers) {
        return new ArrayList<>(integers);
    }

    @Override
    public String toString() {
        return "MarkovChain@" + this.hashCode() + ":://" + numOfChars + "//" + Arrays.toString(absorbingState);
    }
}
