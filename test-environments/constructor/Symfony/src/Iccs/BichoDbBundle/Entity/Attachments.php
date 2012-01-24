<?php

namespace Iccs\BichoDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\BichoDbBundle\Entity\Attachments
 */
class Attachments
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var integer $issueId
     */
    private $issueId;

    /**
     * @var string $name
     */
    private $name;

    /**
     * @var text $description
     */
    private $description;

    /**
     * @var string $url
     */
    private $url;

    /**
     * @var integer $submittedBy
     */
    private $submittedBy;

    /**
     * @var datetime $submittedOn
     */
    private $submittedOn;


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
     * Set issueId
     *
     * @param integer $issueId
     */
    public function setIssueId($issueId)
    {
        $this->issueId = $issueId;
    }

    /**
     * Get issueId
     *
     * @return integer 
     */
    public function getIssueId()
    {
        return $this->issueId;
    }

    /**
     * Set name
     *
     * @param string $name
     */
    public function setName($name)
    {
        $this->name = $name;
    }

    /**
     * Get name
     *
     * @return string 
     */
    public function getName()
    {
        return $this->name;
    }

    /**
     * Set description
     *
     * @param text $description
     */
    public function setDescription($description)
    {
        $this->description = $description;
    }

    /**
     * Get description
     *
     * @return text 
     */
    public function getDescription()
    {
        return $this->description;
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
     * Set submittedBy
     *
     * @param integer $submittedBy
     */
    public function setSubmittedBy($submittedBy)
    {
        $this->submittedBy = $submittedBy;
    }

    /**
     * Get submittedBy
     *
     * @return integer 
     */
    public function getSubmittedBy()
    {
        return $this->submittedBy;
    }

    /**
     * Set submittedOn
     *
     * @param datetime $submittedOn
     */
    public function setSubmittedOn($submittedOn)
    {
        $this->submittedOn = $submittedOn;
    }

    /**
     * Get submittedOn
     *
     * @return datetime 
     */
    public function getSubmittedOn()
    {
        return $this->submittedOn;
    }
}