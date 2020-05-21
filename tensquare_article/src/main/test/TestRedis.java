import com.bwie.article.ArticleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @描述：
 * @作者：zhangyuyang
 * @日期：2020/5/19 9:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArticleApplication.class)
public class TestRedis {


    @Autowired
    RedisTemplate redisTemplate ;

    @Test
    public void set(){
        redisTemplate.opsForValue().set("1712a","你好");
    }

    @Test
    public void get(){
        String name = (String) redisTemplate.opsForValue().get("1712a");
        System.out.println(name);
    }


    /**
     * 模拟，发送短信验证码
     */
    @Test
    public void codePhone(){
        String  phone = "18500348106";
        String code = "158852";
        //发送验证码，并且把验证码存到redis 设置一个过期时间为10分钟
        //redisTemplate.opsForValue().set(phone,code,600, TimeUnit.SECONDS);
        //举例因为是测试，所有设置验证码时间为20秒
        redisTemplate.opsForValue().set(phone,code,60, TimeUnit.SECONDS);

    }

    /***
     * 用户输入验证码，去redis获取验证码，对比，如果一致则继续往下进行业务操作
     */
    @Test
    public void chekcCode(){
        String  phone = "18500348106";
        String code = "158855";
       String redisCode = (String) redisTemplate.opsForValue().get(phone);
        if(redisCode!=null){
            if(code.equals(redisCode)){
                System.out.println("验证码验证通过");
            }else{
                System.out.println("验证码输入错误，请重新输入");
            }
        }else{
            System.out.println("验证码已失效，请重新获取");
        }
    }
}
