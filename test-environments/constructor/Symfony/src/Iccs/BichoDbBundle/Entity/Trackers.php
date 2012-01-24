<?php

namespace Iccs\BichoDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\BichoDbBundle\Entity\Trackers
 */
class Trackers
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var string $url
     */
    private $url;

    /**
     * @var integer $type
     */
    private $type;

    /**
     * @var datetime $retrievedOn
     */
    private $retrievedOn;


    /**
     * Get id
     *
     * @return integer 
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set url
     *
     * @param string $url
     */
    public function setUrl($url)
    {
        $this->url = $url;
    }

    /**
     * Get url
     *
     * @return string 
     */
    public function getUrl()
    {
        return $this->url;
    }

    /**
     * Set type
     *
     * @param integer $type
     */
    public function setType($type)
    {
        $this->type = $type;
    }

    /**
     * Get type
     *
     * @return integer 
     */
    public function getType()
    {
        return $this->type;
    }

    /**
     * Set retrievedOn
     *
     * @param datetime $retrievedOn
     */
    public function setRetrievedOn($retrievedOn)
    {
        $this->retrievedOn = $retrievedOn;
    }

    /**
     * Get retrievedOn
     *
     * @return datetime 
     */
    public function getRetrievedOn()
    {
        return $this->retrievedOn;
    }
}