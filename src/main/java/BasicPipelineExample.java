import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;
import edu.stanford.nlp.util.PropertiesUtils;

import java.util.*;

public class BasicPipelineExample {

    public static void main(String[] args) {

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        StanfordCoreNLP pipeline = new StanfordCoreNLP(
                PropertiesUtils.asProperties(
                        "annotators", "tokenize,ssplit,pos,lemma,parse,natlog,stopword,sentiment",
                        "customAnnotatorClass.stopword", "StopwordAnnotator",
                        "parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz",
                        "tokenize.language", "en"));

        System.out.println("initialised pipeline");
        System.out.println();

        // read some text in the text variable
        String text = "this is some text I hope you like reading it jeremy this is another sentence to see if it can intelligently figure it out without punctuation";
//        String text = "this is horrible, I hate it more than anything!";
//        String text = "this is amazing, I absolutely love it!";
//        String text = "The apple tree (Malus pumila, commonly and erroneously called Malus domestica) is a deciduous tree in the rose family best known for its sweet, pomaceous fruit, the apple. It is cultivated worldwide as a fruit tree, and is the most widely grown species in the genus Malus. The tree originated in Central Asia, where its wild ancestor, Malus sieversii, is still found today. Apples have been grown for thousands of years in Asia and Europe, and were brought to North America by European colonists. Apples have religious and mythological significance in many cultures, including Norse, Greek and European Christian traditions.\n" +
//                "\n" +
//                "Apple trees are large if grown from seed. Generally apple cultivars are propagated by grafting onto rootstocks, which control the size of the resulting tree. There are more than 7,500 known cultivars of apples, resulting in a range of desired characteristics. Different cultivars are bred for various tastes and uses, including cooking, eating raw and cider production. Trees and fruit are prone to a number of fungal, bacterial and pest problems, which can be controlled by a number of organic and non-organic means. In 2010, the fruit's genome was sequenced as part of research on disease control and selective breeding in apple production.";
//        String text = "Hello to all my translators being held hostage! It’s Beautiful/Anonymous. One hour, one phone call, no names, no holds barred. Hey everybody it’s Chris Gethard here. Another episode of  Beautiful/Anonymous. You can hear it in my voice -- a little gravely. Guess what? I’m just coming off the road. I’m back in New York after two weeks of being on the road - meeting all you guys face to face. Starting on Vancouver we made our way all over North America and it’s been so cool to shake your guys hands. Thank for listening, and also -- good huggers. That’s one thing I’ve learned about  Beautiful/Anonymous fans out there are people who enjoy a good and at times inappropriately long hug. Thank you for all the hugs.\n" +
//                "\n" +
//                "\n" +
//                "Listen, if you do wanna see us we’re back one night only. This episode’s dropping Tuesday, June 13th. Final show, Asbury Park, New Jersey. There are still tickets left, I need Jersey to step up. I’ve been telling everybody, man. Jersey my home town -- looks like we’re not going to see it out. That’s okay. I have to be humble about that. We don’t have to sell out. Point being, though. You’re in Jersey, you’re nearby, we’ve got two shows at House of Independance. ChrisGeth.com you can get tickets right now. I wanna have a blowout. I wanna party on the boardwalk after we finish this tour. It’s gonna be like Bash at the Beach, man. Like the old WCW Wrestling event, we’re gonna have a Bash at the Beach. If you’re downloading this the day it comes out it’s tonight. Get yourself to Jersey! Tell your boss, “Hey, I’m getting out of here early. Cuttin out early. Gotta go party and get emo and listen to a phone call.” I’ll see you tonight at Asbury Park, it’ll be good.\n" +
//                "\n" +
//                "\n" +
//                "Last week’s episode was one of our live episodes from the tour. We uploaded that from the road. It was from Portland, the Helium Comedy Club. Lot of feedback from people saying it was an extremely  funny episode which I greatly appreciate. Also a lot of feedback from people saying that they felt bad for the caller and that they weren’t too  thrilled on me. I wanna go ahead and say: it’s valid. I get it. I don’t disagree. A lot of people saying that with the live crowd there they could feel that I need need to entertain that crowd and maybe the caller had a less intimate experience. A lot of people saying that I threw the initial caller under the bus, saying she dropped the ball and that her sister saved it, and that I was pandering to the crowd a little bit. I think there’s some truth to that. I’m not gonna argue with you.\n" +
//                "\n" +
//                "\n" +
//                "I think if you ever see - one thing we’ve all learned from this tour - if you see me tweet it out -- tweet out the picture - and there’s a big crowd behind me you know you’re getting a slightly different experience. And I’ve learned that, and I do wanna apologize to the 24-year-old caller from the Portland call. If you felt put upon. In my heard, in my gut, what I felt onstage that night was that she and I were in it together. We both felt that tension, we were laughing about it. My hope is that it felt like there was camaraderie there but I understand listening to it seemed harsher than the normal call, and for anyone who wasn’t into that: I get it. I do think it was funny, I do think the live calls are funny. I do think we’re gonna put some more of them out there in the world. I’ll tell you in the coming weeks how you guys can get access to all of them. Some people don’t want em, some people do. We’re gonna figure that out, we’ll let you know. They’ve been a really wonderful time and that Baltimore one in particular, when you guys hear that one.. Ooh. You’re going to lose your minds.\n" +
//                "\n" +
//                "\n" +
//                "Now, let’s talk about this week's’ call cause I’m so excited and I have been so excited. We’ve been sitting on this one for a month or two but we gotta get this one right. Cause it’s one of the most special calls that’s ever happend if you ask me. I always remind myself: I am so lucky I get to do this I get talk to people who live so differently than I do. And it’s a blessing, it’s a blessing to see that there’s people from all different walks of life. And I always figured certain types of people who Im never gonna be able to talk to cause maybe they don’t know about podcast, maybe they come from different circumstances where  that’s just not a factor in their lives, and I’ll say: I was a little ignorant. The caller today comes from a world where I just thought, there’s no way they’d consume a podcast. There’s no way!\n" +
//                "\n" +
//                "\n" +
//                "Um, I was wrong. I was totally wrong. And my mind was blown. And I’m glad it was blown. I got to hear about a life that I didn’t ever think I’d get the insider’s look at. I’m so grateful that it happened. It’s a really cool crazy thing that I got to participate in. I’m so psyched about it and I think you’re gonna be psyched about it too.\n" +
//                "\n" +
//                "\n" +
//                "I got fired up. We got so fired up here at Earwolf that we’re actually doing a thing. Wanan put this out here: end of the episode we’re selling a piece of merchandise. It ties into the episode you’re about to hear. Some of you guys turn up the episodes before the outros. This week, I’m just asking you, as a favor to me stick around. Hear about this thing. I’m not making any money off of this piece of merchandise, it’s towards a greater purpose, it relates to this call.\n" +
//                "\n" +
//                "\n" +
//                "I’m so psyched. I don’t wanna say too much, I’ve been talking too much already. It’s one of my favorite calls. Think it’s gonna be one of your favorite calls too. Let’s get into it.";
//
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);


        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                Pair stop = token.get(StopwordAnnotator.class);
                boolean isStop = (boolean)stop.first || (boolean)stop.second;
                System.out.println(String.format("%s %s %s %s %b %b %b", word, pos, ne, lemma, stop.first, stop.second, isStop));
            }

        }
        System.out.println();
        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                Pair stop = token.get(StopwordAnnotator.class);
                if((boolean)stop.first || (boolean)stop.second) continue;
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);

                System.out.println(lemma);
            }
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
            System.out.println(". {" + sentiment + "}");
        }

    }

}