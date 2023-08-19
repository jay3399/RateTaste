package com.example.ratetaste.domain.review.infrastructure;

import java.util.ArrayList;
import java.util.List;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.Builder;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.nd4j.linalg.factory.Nd4j;


public class AnalysisModel {

    private MultiLayerNetwork model;

    public AnalysisModel() {

        int inputNum = 20; // 입력 노드의 수
        int outputNum = 1;  // 출력 노드의 수 (평점 예측

        MultiLayerConfiguration config = new Builder()
                .updater(new Adam(0.01))
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .l2(0.0001)
                .list()
                .layer(0, new DenseLayer.Builder().nIn(inputNum).nOut(500).build())
                .layer(1, new DenseLayer.Builder().nIn(500).nOut(250).build())
                .layer(2, new OutputLayer.Builder(LossFunction.MEAN_SQUARED_LOGARITHMIC_ERROR)
                        .activation(Activation.IDENTITY).nIn(250).nOut(outputNum).build())
                .build();

        model = new MultiLayerNetwork(config);
        model.init();

    }

    public void train(MultiLayerNetwork model , List<String> reviews ,List<Float> scores , Word2Vec word2Vec) {

        List<DataSet> dataSets = new ArrayList<>();

        for (int i = 0; i < reviews.size(); i++) {
            String review = reviews.get(i);
            Float score = scores.get(i);

            INDArray features = transformTextToVector(review, word2Vec);
            INDArray labels = Nd4j.create(new float[]{score});

            DataSet dataSet = new DataSet(features, labels);

            dataSets.add(dataSet);


        }

        DataSetIterator dataSetIterator = new ListDataSetIterator<>(dataSets);

        int count = 10;

        for (int i = 0; i < count; i++) {
            model.fit(dataSetIterator);
        }



    }


    private INDArray transformTextToVector(String text, Word2Vec word2Vec) {
        int layerSize = word2Vec.getLayerSize();
        INDArray vector = Nd4j.zeros(1, layerSize);

        String[] words = text.split(" ");

        for (String word : words) {
            if (word2Vec.hasWord(word)) {
                vector.addi(word2Vec.getWordVectorMatrix(word));
            }
        }

        return vector.divi(words.length);


    }








}
