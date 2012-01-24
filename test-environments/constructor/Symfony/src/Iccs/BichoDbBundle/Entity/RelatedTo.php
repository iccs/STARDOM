<?php

namespace Iccs\BichoDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\BichoDbBundle\Entity\RelatedTo
 */
class RelatedTo
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
     * @var integer $relatedTo
     */
    private $relatedTo;

    /**
     * @var integer $type
     */
    private $type;


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
     * Set relatedTo
     *
     * @param integer $relatedTo
     */
    public function setRelatedTo($relatedTo)
    {
        $this->relatedTo = $relatedTo;
    }

    /**
     * Get relatedTo
     *
     * @return integer 
     */
    public function getRelatedTo()
    {
        return $this->relatedTo;
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
}