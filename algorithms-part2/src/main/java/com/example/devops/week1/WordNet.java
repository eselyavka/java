package com.example.devops.week1;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

public class WordNet {
    private Map<Integer, String> synSetIdMap;
    private Map<String, Bag<Integer>> synSetNounMap;
    private Digraph G;
    private int synSetCount;
    private SAP sap;

    public WordNet(String synset, String hypernyms) {
        if (synset == null || hypernyms == null) {
            throw new IllegalArgumentException("null input");
        }

        populateSynSet(synset);
        buildDAG(hypernyms);
        sap = new SAP(G);
    }

    private void populateSynSet(String synset) {
        synSetIdMap = new HashMap<Integer, String>();
        synSetNounMap = new HashMap<String, Bag<Integer>>();

        In fsynset = new In(synset);
        Bag<Integer> ids;

        synSetCount = 0;
        while (fsynset.hasNextLine()) {
            synSetCount++;
            String[] payload = fsynset.readLine().split(",");
            int synSetId = Integer.parseInt(payload[0]);
            synSetIdMap.put(synSetId, payload[1]);
            for (String noun : payload[1].split(" ")) {
                ids = synSetNounMap.get(noun);
                if (null == ids) {
                    ids = new Bag<Integer>();
                    ids.add(synSetId);
                    synSetNounMap.put(noun, ids);
                } else {
                    ids.add(synSetId);
                }
            }
        }
    }

    public Iterable<String> nouns() {
        return synSetNounMap.keySet();
    }

    public boolean isNoun(String word) {
        if (null == word) {
            throw new IllegalArgumentException("null input");
        }
        return synSetNounMap.containsKey(word);
    }

    private boolean isMultipleRoot(Digraph digraph) {
        int root = 0;
        int size = digraph.V();
        for (int i = 0; i < size; i++) {
            if (!digraph.adj(i).iterator().hasNext()) {
                root += 1;
            }
        }
        return root != 1;
    }

    private boolean isRootedDAG(Digraph digraph) {
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        return directedCycle.hasCycle();
    }

    private void buildDAG(String hypernyms) {
        In fhypernyms = new In(hypernyms);
        G = new Digraph(synSetCount);

        while (fhypernyms.hasNextLine()) {
            String[] payload = fhypernyms.readLine().split(",");
            if (payload.length > 1) {
                for (int i = 1; i < payload.length; i++) {
                    G.addEdge(Integer.parseInt(payload[0]), Integer.parseInt(payload[i]));
                }
            }
        }

        if (isRootedDAG(G) || isMultipleRoot(G)) {
            throw new IllegalArgumentException("null input");
        }
    }

    private void isValidNouns(String nounA, String nounB) {
        if (null == nounA || null == nounB) {
            throw new IllegalArgumentException("null input");
        }

        if (!(isNoun(nounA) && isNoun(nounB))) {
            throw new IllegalArgumentException("must be valid nouns");
        }

    }

    public int distance(String nounA, String nounB) {
        isValidNouns(nounA, nounB);
        return sap.length(synSetNounMap.get(nounA), synSetNounMap.get(nounB));
    }

    public String sap(String nounA, String nounB) {
        isValidNouns(nounA, nounB);
        return synSetIdMap.get(sap.ancestor(synSetNounMap.get(nounA), synSetNounMap.get(nounB)));
    }

    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        StdOut.println(wordNet.distance("gluten", "histone"));
        StdOut.println(wordNet.sap("gluten", "histone"));
    }
}
