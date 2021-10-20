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

    // 1. формируем отсортированную карту на основании данных из списка vassalWithBoss
    public SortedMap<String, Object> getSortedResult(){
        //заполняем vassalWithBoss
        getVassalWithBossMap();
        for (var entry : vassalWithBoss.entrySet()) {
            //заполняем верхний уровень вассалов, которые подчиняются только королю
            if(entry.getValue().equals("king")){
                //если у данного вассала есть подчиненные
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

    // 2. формируем из списка pollResults карту vassalWithBoss, где ключ - вассал, значение - руководитель
    private Map<String, String> getVassalWithBossMap(){
        for(String vassalString : pollResults) {
            //если персона является руководителем
            if(vassalString.contains(":")){
                //извлекаем имя персоны
                String boss = vassalString.substring(0 , vassalString.indexOf(":"));
                //извлекаем его подчинённых
                String vassals = vassalString.substring(vassalString.indexOf(":") + 2);
                String[] splitVassals = vassals.split(", ");
                for (int i = 0; i < splitVassals.length; i++) {
                    //заносим в vassalWithBoss подчиненных с его именем в "value"
                    vassalWithBoss.put(splitVassals[i], boss);
                }
                //проверяем, подчиняется ли сам руководитель кому-нибудь кроме короля
                // , если нет вносим его в vassalWithBoss с значением "king"
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

    // 3. получение вассалов по имени руководителя
    private SortedMap<String, Object> getVassalsByBossName(String bossName) {
        SortedMap<String, Object> vassals = new TreeMap<>();
        for (var entry : vassalWithBoss.entrySet()) {
            //если у этого руководителя есть подчиненные
            if(entry.getValue().equals(bossName)){
                //является ли найденный подчиненный, в свою очередь тоже руководителем
                // - спуск по иерархии вниз, с перезапопуском методом самого себя, пока не дойдет до конца ветки
                // подчиненных
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
