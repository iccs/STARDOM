<?php

namespace Iccs\CvsanalyDbBundle\Entity;

use Doctrine\ORM\Mapping as ORM;

/**
 * Iccs\CvsanalyDbBundle\Entity\FileLinks
 */
class FileLinks
{
    /**
     * @var integer $id
     */
    private $id;

    /**
     * @var integer $parentId
     */
    private $parentId;

    /**
     * @var integer $fileId
     */
    private $fileId;

    /**
     * @var integer $commitId
     */
    private $commitId;


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
     * Set parentId
     *
     * @param integer $parentId
     */
    public function setParentId($parentId)
    {
        $this->parentId = $parentId;
    }

    /**
     * Get parentId
     *
     * @return integer 
     */
    public function getParentId()
    {
        return $this->parentId;
    }

    /**
     * Set fileId
     *
     * @param integer $fileId
     */
    public function setFileId($fileId)
    {
        $this->fileId = $fileId;
    }

    /**
     * Get fileId
     *
     * @return integer 
     */
    public function getFileId()
    {
        return $this->fileId;
    }

    /**
     * Set commitId
     *
     * @param integer $commitId
     */
    public function setCommitId($commitId)
    {
        $this->commitId = $commitId;
    }

    /**
     * Get commitId
     *
     * @return integer 
     */
    public function getCommitId()
    {
        return $this->commitId;
    }
}