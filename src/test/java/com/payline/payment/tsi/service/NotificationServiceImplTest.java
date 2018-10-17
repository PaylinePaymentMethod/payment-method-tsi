package com.payline.payment.tsi.service;

import com.payline.pmapi.bean.notification.request.NotificationRequest;
import com.payline.pmapi.bean.notification.response.NotificationResponse;
import com.payline.pmapi.bean.notification.response.impl.IgnoreNotificationResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith( MockitoJUnitRunner.class )
public class NotificationServiceImplTest {

    @InjectMocks
    private NotificationServiceImpl service;

    /*
    Can't really do better than that given that the method doesn't do much...
     */
    @Test
    public void testParse_notNull(){
        // when: parse method is called
        NotificationResponse response = service.parse( mock( NotificationRequest.class ) );

        // then: result is not null
        Assert.assertNotNull( response );

        Assert.assertTrue(response instanceof IgnoreNotificationResponse);

        Assert.assertEquals(new Integer(200), ((IgnoreNotificationResponse)response).getHttpStatus());
        Assert.assertEquals("ACC=OK", ((IgnoreNotificationResponse)response).getHttpBody());
    }

}
