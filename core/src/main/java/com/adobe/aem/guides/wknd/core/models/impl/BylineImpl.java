package com.adobe.aem.guides.wknd.core.models.impl;
import java.util.List;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collections;

import com.adobe.aem.guides.wknd.core.models.Byline;
import com.adobe.cq.wcm.core.components.models.Image;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;


@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {Byline.class},
        resourceType = {BylineImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BylineImpl implements Byline {
    protected static final String RESOURCE_TYPE = "wknd/components/byline";

    //In order to get image
    //Alternative to get image directly from the request using
    //@Self
    //private Image image;
    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private ModelFactory modelFactory;

    @ValueMapValue
    private String name;

    @ValueMapValue
    private List<String> occupations;

    private Image image;

    /**
    * @PostConstruct is immediately called after the class has been initialized
    * but BEFORE any of the other public methods.
    * It is a good method to initialize variables that will be used by methods in the rest of the model
    * Sort of like a constructor
    */
    @PostConstruct
    private void init() {
        image = modelFactory.getModelFromWrappedRequest(request,
                                                        request.getResource(),
                                                        Image.class);
    }

    /**
    * @return the Image Sling Model of this resource, or null if the resource cannot create a valid Image Sling Model.
    */
    private Image getImage() {
        return image;
    }

    /* (non-Javadoc)
     * @see com.adobe.aem.guides.wknd.core.models.Byline#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getOccupations() {
        if(occupations !=null) {
            Collections.sort(occupations);
            return new ArrayList<String>(occupations);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isEmpty() {
        final Image componentImage = getImage();
        if(StringUtils.isBlank(name)) {
            //name is missing but required
            return true;
        } else if (occupations == null || occupations.isEmpty()){
            //at least one occupation is required
            return true;
        } else if (componentImage == null || StringUtils.isBlank(componentImage.getSrc())) {
            //a valid image is required
            return true;
        } else {
            //all fields populated
            return false;
        }
    }
}
