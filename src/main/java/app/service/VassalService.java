package app.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VassalService {
    private List<String> pollResults = List.of(
            "служанка Аня",
            "управляющий Семен Семеныч: крестьянин Федя, доярка Нюра",
            "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна",
            "экономка Лидия Федоровна: дворник Гена, служанка Аня",
            "доярка Нюра",
            "кот Василий: человеческая особь Катя",
            "дворник Гена: посыльный Тошка",
            "киллер Гена",
            "зажиточный холоп: крестьянка Таня",
            "секретарь короля: зажиточный холоп, шпион Т",
            "шпион Т: кучер Д",
            "посыльный Тошка: кот Василий",
            "аристократ Клаус",
            "просветленный Антон"
    );

    private Map<String, String> vassalWithBoss = new HashMap<>();
    private SortedMap<String, Object> sortedResult = new TreeMap<>();

    public SortedMap<String, Object> getSortedResult(){
        getVassalWithBossMap();
        for (var entry : vassalWithBoss.entrySet()) {
            if(entry.getValue().equals("king")){
                if (!getVassalsByBossName(entry.getKey()).isEmpty()) {
                    sortedResult.put(entry.getKey(), getVassalsByBossName(entry.getKey()));
                }
                else {
                    sortedResult.put(entry.getKey(), "");
                }
            }
        }
        return sortedResult;
    }

    private Map<String, String> getVassalWithBossMap(){
        for(String vassalString : pollResults) {
            if(vassalString.contains(":")){
                String vassals = vassalString.substring(vassalString.indexOf(":") + 2);
                String boss = vassalString.substring(0 , vassalString.indexOf(":"));
                String[] splitVassals = vassals.split(", ");
                for (int i = 0; i < splitVassals.length; i++) {
                    vassalWithBoss.put(splitVassals[i], boss);
                }
                if(!vassalWithBoss.containsKey(boss)) {
                    vassalWithBoss.put(boss, "king");
                }
            }
            else {
                if(!vassalWithBoss.containsKey(vassalString)){
                    vassalWithBoss.put(vassalString, "king");
                }
            }
        }
        return vassalWithBoss;
    }

    private SortedMap<String, Object> getVassalsByBossName(String bossName) {
        SortedMap<String, Object> vassals = new TreeMap<>();
        for (var entry : vassalWithBoss.entrySet()) {
            if(entry.getValue().equals(bossName)){
                if (!getVassalsByBossName(entry.getKey()).isEmpty()) {
                    vassals.put(entry.getKey(), getVassalsByBossName(entry.getKey()));
                }
                else {
                    vassals.put(entry.getKey(), "");
                }
            }
        }
        return vassals;
    }
}
