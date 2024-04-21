package com.example.facial_feedback_app.feature_record.domain.data_analysis

import com.example.facial_feedback_app.feature_record.domain.Emotions

data class EmotionProbabilitiesPerFrame(
        private val anger:MutableList<Float> = mutableListOf(),
        private val disgust:MutableList<Float> = mutableListOf(),
        private  val fear:MutableList<Float> = mutableListOf(),
        private  val happy:MutableList<Float> = mutableListOf(),
        private  val neutral:MutableList<Float> = mutableListOf(),
        private  val sad:MutableList<Float> = mutableListOf(),
        private  val surprise:MutableList<Float> = mutableListOf(),
) {


        fun getEmotionProbabilityList(emotion: Emotions):List<Float>{

                return when(emotion){
                        Emotions.ANGRY ->anger
                        Emotions.DISGUST -> disgust
                        Emotions.FEAR -> fear
                        Emotions.HAPPY -> happy
                        Emotions.NEUTRAL -> neutral
                        Emotions.SAD -> sad
                        Emotions.SURPRISE -> surprise
                }
        }


        /**
        *
         * our model gives us floatarray of emotions of each face means
         * each float array contains 7 percentage of 7 emotions of single face
         * each frame may contains multiple face so will have multiple float array
         *
         * We dont want /n
         *      list
         *              face -> emotions
         *              face ->emotions
         *              .
         *              .
         *              .face-> emotions
*        * We want
         *        list
         *              emotion(ANGRY)-> probabilities of all face
         *              emotion(DISGUST)-> probabilities of all face
         *              .
         *              . 7 entry
         *              .
         *              emotion(SURPRISE)-> probabilities of all face
         *
         *
*        * so we are converting what we dont want to wat we want using this funtion
         *
        * */
        fun addEmotionProbabilitiesOfFace(emotionProbaOfFace:List<FloatArray>){

                emotionProbaOfFace.forEach {
                        it.forEachIndexed { index, proba ->
                                when(index+1){

                                        1-> this.component1().add(proba)
                                        2-> this.component2().add(proba)
                                        3-> this.component3().add(proba)
                                        4-> this.component4().add(proba)
                                        5-> this.component5().add(proba)
                                        6-> this.component6().add(proba)
                                        7-> this.component7().add(proba)

                                }
                        }
                }

        }
}