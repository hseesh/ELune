package com.yatoufang.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yatoufang.entity.Field;
import com.yatoufang.entity.Param;
import com.yatoufang.test.model.entity.NodeConfig;
import org.apache.commons.compress.utils.Lists;

import java.lang.reflect.Type;
import java.util.*;

class Test {

    public static void main(String[] args) {
        String str = "[{\"name\":\"goodsId\",\"typeAlias\":\"int\"},{\"name\":\"num\",\"typeAlias\":\"int\"},{\"name\":\"endTime\",\"typeAlias\":\"long\"},{\"name\":\"flagId\",\"typeAlias\":\"int\"},{\"name\":\"equipmentId\",\"typeAlias\":\"long\"},{\"name\":\"rewardMap\",\"typeAlias\":\"Map&lt;Integer, Long&gt;\"},{\"name\":\"artifactId\",\"typeAlias\":\"long\"},{\"name\":\"devourMap\",\"typeAlias\":\"Map&lt;Integer, Integer&gt;\"},{\"name\":\"rank\",\"typeAlias\":\"long\"},{\"name\":\"taskId\",\"typeAlias\":\"int\"},{\"name\":\"time\",\"typeAlias\":\"long\"},{\"name\":\"rage\",\"typeAlias\":\"int\"},{\"name\":\"isSkip\",\"typeAlias\":\"boolean\"},{\"name\":\"groupId\",\"typeAlias\":\"int\"},{\"name\":\"goodsIdList\",\"typeAlias\":\"Collection&lt;Integer&gt;\"},{\"name\":\"damage\",\"typeAlias\":\"long\"},{\"name\":\"goodsUid\",\"typeAlias\":\"long\"},{\"name\":\"price\",\"typeAlias\":\"int\"},{\"name\":\"spiritId\",\"typeAlias\":\"long\"},{\"name\":\"targetServeRId\",\"typeAlias\":\"int\"},{\"name\":\"recharge\",\"typeAlias\":\"int\"},{\"name\":\"monsterId\",\"typeAlias\":\"int\"},{\"name\":\"push\",\"typeAlias\":\"boolean\"},{\"name\":\"count\",\"typeAlias\":\"int\"},{\"name\":\"goodsMap\",\"typeAlias\":\"Map&lt;Long, Integer&gt;\"},{\"name\":\"skillLevel\",\"typeAlias\":\"int\"},{\"name\":\"configId\",\"typeAlias\":\"int\"},{\"name\":\"runeId\",\"typeAlias\":\"long\"},{\"name\":\"addExp\",\"typeAlias\":\"long\"},{\"name\":\"actorId\",\"typeAlias\":\"long\"},{\"name\":\"criticalList\",\"typeAlias\":\"Collection&lt;Integer&gt;\"},{\"name\":\"num\",\"typeAlias\":\"long\"},{\"name\":\"battleId\",\"typeAlias\":\"long\"},{\"name\":\"id\",\"typeAlias\":\"int\"},{\"name\":\"change\",\"typeAlias\":\"int\"},{\"name\":\"title\",\"typeAlias\":\"String\"},{\"name\":\"actorName\",\"typeAlias\":\"String\"},{\"name\":\"targetActorId\",\"typeAlias\":\"long\"},{\"name\":\"message\",\"typeAlias\":\"String\"},{\"name\":\"costId\",\"typeAlias\":\"int\"},{\"name\":\"heroIds\",\"typeAlias\":\"Collection&lt;Long&gt;\"},{\"name\":\"chosenId\",\"typeAlias\":\"long\"},{\"name\":\"winnerId\",\"typeAlias\":\"long\"},{\"name\":\"storyId\",\"typeAlias\":\"int\"},{\"name\":\"amount\",\"typeAlias\":\"int\"},{\"name\":\"score\",\"typeAlias\":\"int\"},{\"name\":\"monsterGroupId\",\"typeAlias\":\"int\"},{\"name\":\"attributes\",\"typeAlias\":\"Map&lt;Byte, Object&gt;\"},{\"name\":\"actorAttributeMap\",\"typeAlias\":\"Map&lt;Byte, Object&gt;\"},{\"name\":\"heroId\",\"typeAlias\":\"int\"},{\"name\":\"nationId\",\"typeAlias\":\"long\"},{\"name\":\"reinforceLevel\",\"typeAlias\":\"int\"},{\"name\":\"nextId\",\"typeAlias\":\"int\"},{\"name\":\"goodsId\",\"typeAlias\":\"long\"},{\"name\":\"ids\",\"typeAlias\":\"Collection&lt;Long&gt;\"},{\"name\":\"star\",\"typeAlias\":\"int\"},{\"name\":\"talentId\",\"typeAlias\":\"int\"},{\"name\":\"fragmentId\",\"typeAlias\":\"int\"},{\"name\":\"level\",\"typeAlias\":\"int\"},{\"name\":\"zoneId\",\"typeAlias\":\"int\"},{\"name\":\"floor\",\"typeAlias\":\"int\"},{\"name\":\"bossId\",\"typeAlias\":\"int\"},{\"name\":\"configIds\",\"typeAlias\":\"Collection&lt;Integer&gt;\"},{\"name\":\"progress\",\"typeAlias\":\"int\"},{\"name\":\"heroId\",\"typeAlias\":\"long\"},{\"name\":\"battleType\",\"typeAlias\":\"int\"},{\"name\":\"treasureId\",\"typeAlias\":\"int\"},{\"name\":\"name\",\"typeAlias\":\"String\"},{\"name\":\"equipmentIds\",\"typeAlias\":\"Collection&lt;Long&gt;\"},{\"name\":\"nationIdList\",\"typeAlias\":\"Collection&lt;Long&gt;\"},{\"name\":\"loserId\",\"typeAlias\":\"long\"},{\"name\":\"isFree\",\"typeAlias\":\"boolean\"},{\"name\":\"devourIds\",\"typeAlias\":\"Collection&lt;Long&gt;\"},{\"name\":\"originalId\",\"typeAlias\":\"long\"},{\"name\":\"petId\",\"typeAlias\":\"long\"},{\"name\":\"content\",\"typeAlias\":\"String\"},{\"name\":\"openTime\",\"typeAlias\":\"long\"},{\"name\":\"chapterId\",\"typeAlias\":\"int\"},{\"name\":\"targetId\",\"typeAlias\":\"long\"},{\"name\":\"activityId\",\"typeAlias\":\"int\"},{\"name\":\"isWind\",\"typeAlias\":\"boolean\"},{\"name\":\"id\",\"typeAlias\":\"long\"},{\"name\":\"giftId\",\"typeAlias\":\"int\"},{\"name\":\"roomId\",\"typeAlias\":\"long\"},{\"name\":\"pets\",\"typeAlias\":\"Map&lt;Integer, Long&gt;\"},{\"name\":\"isRoll\",\"typeAlias\":\"boolean\"},{\"name\":\"skillType\",\"typeAlias\":\"int\"},{\"name\":\"stateId\",\"typeAlias\":\"int\"},{\"name\":\"positionId\",\"typeAlias\":\"int\"},{\"name\":\"treasureId\",\"typeAlias\":\"long\"},{\"name\":\"teamId\",\"typeAlias\":\"long\"},{\"name\":\"titleId\",\"typeAlias\":\"int\"},{\"name\":\"effectId\",\"typeAlias\":\"int\"},{\"name\":\"runeIds\",\"typeAlias\":\"Collection&lt;Long&gt;\"},{\"name\":\"storyIdList\",\"typeAlias\":\"List&lt;Integer&gt;\"},{\"name\":\"laseResetTime\",\"typeAlias\":\"long\"},{\"name\":\"times\",\"typeAlias\":\"int\"},{\"name\":\"serverId\",\"typeAlias\":\"int\"},{\"name\":\"beginTime\",\"typeAlias\":\"long\"},{\"name\":\"orderId\",\"typeAlias\":\"int\"},{\"name\":\"power\",\"typeAlias\":\"long\"},{\"name\":\"points\",\"typeAlias\":\"int\"},{\"name\":\"times\",\"typeAlias\":\"Map&lt;Long, Integer&gt;\"},{\"name\":\"fragmentIds\",\"typeAlias\":\"Collection&lt;Long&gt;\"},{\"name\":\"orderIds\",\"typeAlias\":\"Collection&lt;String&gt;\"},{\"name\":\"replaceId\",\"typeAlias\":\"long\"},{\"name\":\"type\",\"typeAlias\":\"int\"},{\"name\":\"costValue\",\"typeAlias\":\"double\"},{\"name\":\"fragmentId\",\"typeAlias\":\"List&lt;Integer&gt;\"},{\"name\":\"isWin\",\"typeAlias\":\"boolean\"},{\"name\":\"positions\",\"typeAlias\":\"Map&lt;Integer, Long&gt;\"},{\"name\":\"isAddExp\",\"typeAlias\":\"boolean\"},{\"name\":\"uid\",\"typeAlias\":\"long\"},{\"name\":\"chargeId\",\"typeAlias\":\"int\"}]";
        Type type = new TypeToken<List<Param>>() {
        }.getType();
        List<Param> list = new Gson().fromJson(str, type);
        StringBuilder builder = new StringBuilder();
        for (Param param : list) {
            String dd = "    <template\n" +
                    "            name=\"" + param.getName() + "\"\n" +
                    "            value=\"" + param.getTypeAlias() + " " + param.getName() + "\"\n" +
                    "            description=\"" + param.getTypeAlias() + " " + param.getName() + "\"\n" +
                    "            toReformat=\"true\"\n" +
                    "            toShortenFQNames=\"false\">\n" +
                    "        <context>\n" +
                    "            <option name=\"ELUNE\" value=\"true\"/>\n" +
                    "        </context>\n" +
                    "    </template>\n";
            builder.append(dd);
        }
        System.out.println(builder.toString());

    }


}
